package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.employeeDto.*;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.Role;
import aitho.ranim.hrms.exception.EmployeeException;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.IRoleRepository;
import aitho.ranim.hrms.service.IEmailService;
import aitho.ranim.hrms.service.IEmployeeService;
import aitho.ranim.hrms.utils.EmployeeUtils;
import aitho.ranim.hrms.viewmodel.EmployeeViewModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService implements IEmployeeService {

    private final IRoleRepository roleRepository;
    private final IEmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    @Override
    public CreateEmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee = EmployeeUtils.createEmployeeFromRequest(request);

        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new EmployeeException("Role not found: " + request.role(), HttpStatus.BAD_REQUEST, "/employee"));
        employee.setRoles(Set.of(role));
        employee.setPassword(passwordEncoder.encode(request.password()));
        employee.setStatus("PENDING");
        employee.setActivationToken(UUID.randomUUID().toString());


        Employee savedEmployee = employeeRepository.save(employee);

        String activationLink = "http://localhost:8080/view/v1/employee/activate/" + savedEmployee.getActivationToken();
        emailService.sendActivationEmail(savedEmployee, activationLink);
        return new CreateEmployeeResponse(
                LocalDate.now(),
                "Employee profile successfully created"
        );
    }


    public EmployeeViewModel activateEmployee(String token) {
        Employee employee = employeeRepository
                .findByActivationToken(token)
                .orElseThrow(() -> new EmployeeException("Invalid activation token", HttpStatus.NOT_FOUND, "/activate"));
        employee.setStatus("ACTIVE");
        employee.setActivationToken(null);
        employeeRepository.save(employee);
        emailService.sendWelcomeEmail(employee);
        return new EmployeeViewModel(
                employee.getFirstName(),
                employee.getLastName()
        );
    }


    @Override
    public EmployeeDetailResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeException("Employee not found", HttpStatus.NOT_FOUND, "/employee/" + id));
        return EmployeeUtils.toEmployeeDetailResponse(employee);

    }

    @Override
    public Page<EmployeeSummaryResponse> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(EmployeeUtils::toEmployeeSummaryResponse);
    }


    @Override
    public UpdateEmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeException("Employee not found", HttpStatus.NOT_FOUND, "/employee/" + id));
        EmployeeUtils.updateEmployeeFromRequest(employee, request);
        employeeRepository.save(employee);
        return new  UpdateEmployeeResponse(
                LocalDate.now(),
                "Employee successfully updated"
        );
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeException("Employee not found", HttpStatus.NOT_FOUND, "/employee/" + id));
        employeeRepository.deleteById(id);
    }
}
