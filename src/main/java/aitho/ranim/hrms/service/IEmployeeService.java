package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.*;
import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.viewmodel.EmployeeViewModel;
import java.util.List;

public interface IEmployeeService {
    CreateEmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeDetailResponse getEmployeeById(Long id);

    List<EmployeeSummaryResponse> getAllEmployees();

    UpdateEmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request);

    void deleteEmployee(Long id);

    EmployeeViewModel activateEmployee(String token);
}
