package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.*;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.exception.EmployeeException;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.service.IEmployeeService;
import aitho.ranim.hrms.utils.EmployeeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService implements IEmployeeService {

    private final IEmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CreateEmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee = EmployeeUtils.createEmployeeFromRequest(request);
        employee.setPassword(passwordEncoder.encode(request.password()));
        employee.setStatus("PENDING");
        employee.setActivationToken(UUID.randomUUID().toString());
        log.info("Email simulation: Click here http://localhost:8080/api/v1/employee/activate?token={}", employee.getActivationToken());
        Employee savedEmployee = employeeRepository.save(employee);

        return new CreateEmployeeResponse(
                LocalDate.now(),
                "Employee successfully created"
        );
    }

    public ActivateEmployeeResponse activateEmployee(String token, ActivateAccountRequest request) {
        Employee employee = employeeRepository
                .findByActivationToken(token)
                .orElseThrow(() -> new EmployeeException("Invalid activation token", HttpStatus.NOT_FOUND, "/activate"));
        employee.setPassword(passwordEncoder.encode(request.newPassword()));
        employee.setStatus("ACTIVE");
        employee.setActivationToken(null);
        employeeRepository.save(employee);

        return new ActivateEmployeeResponse(
                LocalDate.now(),
                "Employee successfully activated"
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
    public List<EmployeeSummaryResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(EmployeeUtils::toEmployeeSummaryResponse)
                .toList();
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
