package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.IEmployeeController;
import aitho.ranim.hrms.dto.*;
import aitho.ranim.hrms.service.IEmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController implements IEmployeeController {
    private final IEmployeeService employeeService;

    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //POST path = api/v1/employee
    @PostMapping
    public ResponseEntity<CreateEmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        CreateEmployeeResponse response = employeeService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    };

    //POST path = http://localhost:8080/api/v1/employee/activate?token={}
    @PostMapping("/activate")
    public ResponseEntity<ActivateEmployeeResponse> activateEmployee(@Valid @RequestParam("token") String token, @RequestBody ActivateAccountRequest request) {
        ActivateEmployeeResponse response = employeeService.activateEmployee(token, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //GET path = http://localhost:8080/api/v1/employee/id
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDetailResponse> getEmployee(@PathVariable Long id) {
        EmployeeDetailResponse response = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public List<EmployeeSummaryResponse> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    //PUT path = http://localhost:8080/api/v1/employee/update/id
    @PutMapping("update/{id}")
    public ResponseEntity<UpdateEmployeeResponse> updateEmployee(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeRequest request) {
        UpdateEmployeeResponse response = employeeService.updateEmployee(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //DELETE path = http://localhost:8080/api/v1/employee/delete/id
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
