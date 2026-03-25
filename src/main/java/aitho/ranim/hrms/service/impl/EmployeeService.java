package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeResponse;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService implements IEmployeeService {

    private final IEmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee  = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setPassword(request.password());
        employee.setEmail(request.email());
        employee.setStatus("PENDING");
        employee.setActivationToken(UUID.randomUUID().toString());

        log.info("Simulazione Email: Clicca qui http://localhost:8080/api/v1/employee/activate?token={}", employee.getActivationToken());
        Employee savedEmployee = employeeRepository.save(employee);
        return new EmployeeResponse(
                LocalDate.now(),
                "Dipendente creato con successo"
        );
    }
}
