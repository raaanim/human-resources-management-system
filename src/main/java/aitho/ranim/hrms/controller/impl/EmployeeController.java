package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.IEmployeeController;
import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeResponse;
import aitho.ranim.hrms.service.IEmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController implements IEmployeeController {
    private final IEmployeeService employeeService;

    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //POST path = api/v1/employee
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    };
}
