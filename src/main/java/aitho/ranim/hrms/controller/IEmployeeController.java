package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IEmployeeController {
    ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request);
}
