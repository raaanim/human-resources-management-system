package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.employeeDto.AssignEmployeeRequest;
import aitho.ranim.hrms.dto.projectAssignmentDto.EmployeeProjectAssignmentResponse;
import aitho.ranim.hrms.dto.projectAssignmentDto.ProjectAssignmentResponse;
import aitho.ranim.hrms.dto.projectAssignmentDto.ProjectEmployeeAssignmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(
        name = "Project Assignment Controller",
        description = "Endpoints for managing employee-project assignments"
)
public interface IProjectAssignmentController {
    @Operation(
            summary = "Assign Employee to Project",
            description = "Assigns an employee to a project with the provided details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee assigned to project successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<ProjectAssignmentResponse> assignEmployeeToProject(@Valid @RequestBody AssignEmployeeRequest request);
    @Operation(
            summary = "Close Project Assignment",
            description = "Closes an active project assignment by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project assignment closed successfully"),
            @ApiResponse(responseCode = "404", description = "Assignment not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<ProjectAssignmentResponse> closeAssignment(@PathVariable Long assignmentId);
    @Operation(
            summary = "Get Employees Assigned to Project",
            description = "Retrieves all employees assigned to a specific project."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<List<EmployeeProjectAssignmentResponse>> getEmployeesAssignedToProject(@PathVariable Long projectId);
    @Operation(
            summary = "Get Projects Assigned to Employee",
            description = "Retrieves all projects assigned to a specific employee."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<List<ProjectEmployeeAssignmentResponse>> getProjectsAssignedToEmployee(@PathVariable Long employeeId);
}
