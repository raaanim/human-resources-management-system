package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.employeeDto.AssignEmployeeRequest;
import aitho.ranim.hrms.dto.projectAssignmentDto.EmployeeProjectAssignmentResponse;
import aitho.ranim.hrms.dto.projectAssignmentDto.ProjectAssignmentResponse;
import aitho.ranim.hrms.dto.projectAssignmentDto.ProjectEmployeeAssignmentResponse;

import java.util.List;

public interface IProjectAssignmentService {

    ProjectAssignmentResponse assignEmployeeToProject(AssignEmployeeRequest request);
    ProjectAssignmentResponse closeProjectAssignment(Long assignmentId);
    List<EmployeeProjectAssignmentResponse> getAssignmentsByProject(Long projectId);
    List<ProjectEmployeeAssignmentResponse> getAssignmentsByEmployee(Long employeeId);

}
