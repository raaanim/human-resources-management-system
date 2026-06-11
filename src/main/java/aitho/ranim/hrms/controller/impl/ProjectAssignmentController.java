package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.IProjectAssignmentController;
import aitho.ranim.hrms.dto.AssignEmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeProjectAssignmentResponse;
import aitho.ranim.hrms.dto.ProjectAssignmentResponse;
import aitho.ranim.hrms.dto.ProjectEmployeeAssignmentResponse;
import aitho.ranim.hrms.service.IProjectAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/project-assignment")
public class ProjectAssignmentController implements IProjectAssignmentController {
    private final IProjectAssignmentService projectAssignmentService;

    public ProjectAssignmentController(IProjectAssignmentService projectAssignmentService) {
        this.projectAssignmentService = projectAssignmentService;
    }

    // POST http://localhost:8080/api/v1/project-assignment/assign
    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ProjectAssignmentResponse> assignEmployeeToProject(@Valid @RequestBody AssignEmployeeRequest request) {
        ProjectAssignmentResponse response = projectAssignmentService.assignEmployeeToProject(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT http://localhost:8080/api/v1/project-assignment/close/{assignmentId}
    @PutMapping("/close/{assignmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ProjectAssignmentResponse> closeAssignment(@PathVariable Long assignmentId) {
        ProjectAssignmentResponse response = projectAssignmentService.closeProjectAssignment(assignmentId);
        return ResponseEntity.ok(response);

    }

    // GET http://localhost:8080/api/v1/project-assignment/project/52
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    public ResponseEntity<List<EmployeeProjectAssignmentResponse>> getEmployeesAssignedToProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectAssignmentService.getAssignmentsByProject(projectId));
    }

    // GET http://localhost:8080/api/v1/project-assignment/employee/9
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    public ResponseEntity<List<ProjectEmployeeAssignmentResponse>> getProjectsAssignedToEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(projectAssignmentService.getAssignmentsByEmployee(employeeId));
    }
}
