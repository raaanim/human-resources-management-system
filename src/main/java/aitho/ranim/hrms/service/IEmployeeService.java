package aitho.ranim.hrms.service;


import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeResponse;
import aitho.ranim.hrms.viewmodel.EmployeeViewModel;


public interface IEmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeViewModel activateEmployee(String token);
}
