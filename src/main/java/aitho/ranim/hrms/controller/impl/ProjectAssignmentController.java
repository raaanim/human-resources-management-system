package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.IProjectAssignmentController;
import aitho.ranim.hrms.dto.employeeDto.AssignEmployeeRequest;
import aitho.ranim.hrms.dto.projectAssignmentDto.EmployeeProjectAssignmentResponse;
import aitho.ranim.hrms.dto.projectAssignmentDto.ProjectAssignmentResponse;
import aitho.ranim.hrms.dto.projectAssignmentDto.ProjectEmployeeAssignmentResponse;
import aitho.ranim.hrms.service.IProjectAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@Tag(name = "Project Assignment Controller", description = "Endpoints for managing project assignments")
@RequestMapping("/api/v1/project-assignment")
public class ProjectAssignmentController implements IProjectAssignmentController {
    private final IProjectAssignmentService projectAssignmentService;

    public ProjectAssignmentController(IProjectAssignmentService projectAssignmentService) {
        this.projectAssignmentService = projectAssignmentService;
    }

    // POST http://localhost:8080/api/v1/project-assignment/assign
    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Assign Employee to Project", description = "Assigns an employee to a project with the provided details.")
    @ApiResponse(responseCode = "201", description = "Employee assigned to project successfully.")
    public ResponseEntity<ProjectAssignmentResponse> assignEmployeeToProject(@Valid @RequestBody AssignEmployeeRequest request) {
        ProjectAssignmentResponse response = projectAssignmentService.assignEmployeeToProject(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT http://localhost:8080/api/v1/project-assignment/close/{assignmentId}
    @PutMapping("/close/{assignmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Operation(summary = "Close Project Assignment", description = "Closes an existing project assignment by its ID.")
    @ApiResponse(responseCode = "200", description = "Project assignment closed successfully.")
    public ResponseEntity<ProjectAssignmentResponse> closeAssignment(@PathVariable Long assignmentId) {
        ProjectAssignmentResponse response = projectAssignmentService.closeProjectAssignment(assignmentId);
        return ResponseEntity.ok(response);

    }

    // GET http://localhost:8080/api/v1/project-assignment/project/52
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Employees Assigned to Project", description = "Retrieves a list of employees assigned to a specific project by its ID.")
    @ApiResponse(responseCode = "200", description = "Employees retrieved successfully.")
    public ResponseEntity<List<EmployeeProjectAssignmentResponse>> getEmployeesAssignedToProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectAssignmentService.getAssignmentsByProject(projectId));
    }

    // GET http://localhost:8080/api/v1/project-assignment/employee/9
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @Operation(summary = "Get Projects Assigned to Employee", description = "Retrieves a list of projects assigned to a specific employee by their ID.")
    @ApiResponse(responseCode = "200", description = "Projects retrieved successfully.")
    public ResponseEntity<List<ProjectEmployeeAssignmentResponse>> getProjectsAssignedToEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(projectAssignmentService.getAssignmentsByEmployee(employeeId));
    }
}
