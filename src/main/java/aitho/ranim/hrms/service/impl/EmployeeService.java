package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.ActivateAccountRequest;
import aitho.ranim.hrms.dto.ActivateEmployeeResponse;
import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeResponse;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.exception.EmployeeException;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.service.IEmailService;
import aitho.ranim.hrms.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService implements IEmployeeService {

    private final IEmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee  = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setPassword(passwordEncoder.encode(request.password()));
        employee.setEmail(request.email());
        employee.setStatus("PENDING");
        employee.setActivationToken(UUID.randomUUID().toString());

        Employee savedEmployee = employeeRepository.save(employee);

        String activationLink = "http://localhost:8080/api/v1/employee/activate/" + savedEmployee.getActivationToken();
        emailService.sendActivationEmail(savedEmployee, activationLink);
        return new EmployeeResponse(
                LocalDate.now(),
                "Employee profile successfully created"
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
        emailService.sendWelcomeEmail(employee);
        return new ActivateEmployeeResponse(
                LocalDate.now(),
                "Employee profile successfully activated"
        );
    }
}
