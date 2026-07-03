package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.employeeDto.*;
import aitho.ranim.hrms.viewmodel.EmployeeViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IEmployeeService {
    CreateEmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeDetailResponse getEmployeeById(Long id);

    Page<EmployeeSummaryResponse> getAllEmployees(Pageable pageable);

    UpdateEmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request);

    void deleteEmployee(Long id);

    EmployeeViewModel activateEmployee(String token);
}
