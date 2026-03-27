package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeResponse;
import aitho.ranim.hrms.entity.Employee;
import jakarta.validation.Valid;

public interface IEmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeResponse activateEmployee(@Valid String token, String s);
}
