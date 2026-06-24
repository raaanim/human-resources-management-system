package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.IEmployeeController;
import aitho.ranim.hrms.dto.*;
import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.service.IEmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@Tag(name = "Employee Controller", description = "Endpoints for managing employees")
@RequestMapping("/api/v1/employee")
public class EmployeeController implements IEmployeeController {
    private final IEmployeeService employeeService;

    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //POST path = http://localhost:8080/api/v1/employee
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PostMapping
    @Operation(summary = "Create employee", description = "Creates a new employee. Only users with ADMIN or HR roles can access this endpoint.")
    @ApiResponse(responseCode = "201", description = "Employee created successfully")
    public ResponseEntity<CreateEmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        CreateEmployeeResponse response = employeeService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    //GET path = http://localhost:8080/api/v1/employee/id
    @PreAuthorize("hasAnyRole('ADMIN', 'HR') or (hasRole('EMPLOYEE') and @employeeSecurity.isEmployeeIdMatching(authentication, #id))")
    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by their ID. Accessible by ADMIN, HR, or the employee themselves.")
    @ApiResponse(responseCode = "200", description = "Employee retrieved successfully")
    public ResponseEntity<EmployeeDetailResponse> getEmployee(@PathVariable Long id) {
        EmployeeDetailResponse response = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @GetMapping
    @Operation(summary = "Get all employees", description = "Retrieves a page of employees with optional filtering and pagination. Accessible by ADMIN and HR.")
    @ApiResponse(responseCode = "200", description = "Employees retrieved successfully")
    public ResponseEntity<Page<EmployeeSummaryResponse>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                employeeService.getAllEmployees(pageable)
        );
    }


    //PATCH path = http://localhost:8080/api/v1/employee/update/id
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PatchMapping ("update/{id}")
    @Operation(summary = "Update employee", description = "Updates an existing employee's details. Only users with ADMIN or HR roles can access this endpoint.")
    @ApiResponse(responseCode = "200", description = "Employee updated successfully")
    public ResponseEntity<UpdateEmployeeResponse> updateEmployee(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeRequest request) {
        UpdateEmployeeResponse response = employeeService.updateEmployee(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //DELETE path = http://localhost:8080/api/v1/employee/delete/id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete employee", description = "Deletes an employee by their ID. Only users with ADMIN role can access this endpoint.")
    @ApiResponse(responseCode = "204", description = "Employee deleted successfully")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
