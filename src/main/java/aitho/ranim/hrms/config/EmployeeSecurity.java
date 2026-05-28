package aitho.ranim.hrms.config;

import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.exception.EmployeeException;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("employeeSecurity")
public class EmployeeSecurity {

    private final IEmployeeRepository employeeRepository;

    public EmployeeSecurity(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public boolean isEmployeeIdMatching(Authentication authentication, Long id) {

        String email = authentication.getName();

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EmployeeException("Email not found", HttpStatus.NOT_FOUND, "employee/{id}"));

        return employee != null && employee.getId().equals(id);
    }
}
