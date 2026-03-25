package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeResponse;
import aitho.ranim.hrms.entity.Employee;

public interface IEmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);


}
