package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.AssignEmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeProjectAssignmentResponse;
import aitho.ranim.hrms.dto.ProjectAssignmentResponse;
import aitho.ranim.hrms.dto.ProjectEmployeeAssignmentResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IProjectAssignmentController {
    ResponseEntity<ProjectAssignmentResponse> assignEmployeeToProject(@Valid @RequestBody AssignEmployeeRequest request);
    ResponseEntity<ProjectAssignmentResponse> closeAssignment(@PathVariable Long assignmentId);
    ResponseEntity<List<EmployeeProjectAssignmentResponse>> getEmployeesAssignedToProject(@PathVariable Long projectId);
    ResponseEntity<List<ProjectEmployeeAssignmentResponse>> getProjectsAssignedToEmployee(@PathVariable Long employeeId);
}
