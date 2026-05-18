package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IEmployeeController {
    ResponseEntity<CreateEmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request);

    ResponseEntity<EmployeeDetailResponse> getEmployee(@PathVariable Long id);

    List<EmployeeSummaryResponse> getAllEmployees();

    ResponseEntity<UpdateEmployeeResponse> updateEmployee(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeRequest request);

    void deleteEmployee(@PathVariable Long id);
}
