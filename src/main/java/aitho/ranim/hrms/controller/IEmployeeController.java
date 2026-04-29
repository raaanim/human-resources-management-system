package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.CreateEmployeeResponse;
import aitho.ranim.hrms.dto.EmployeeRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IEmployeeController {
    ResponseEntity<CreateEmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request);
}
