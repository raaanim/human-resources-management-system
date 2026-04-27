package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.*;
import jakarta.validation.Valid;

import java.util.List;

public interface IEmployeeService {
    CreateEmployeeResponse createEmployee(EmployeeRequest request);

    ActivateEmployeeResponse activateEmployee(String token, ActivateAccountRequest request);

    EmployeeDetailResponse getEmployeeById(Long id);

    List<EmployeeSummaryResponse> getAllEmployees();

    UpdateEmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request);

    void deleteEmployee(Long id);
}
