package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.AssignEmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeProjectAssignmentResponse;
import aitho.ranim.hrms.dto.ProjectAssignmentResponse;
import aitho.ranim.hrms.dto.ProjectEmployeeAssignmentResponse;

import java.util.List;

public interface IProjectAssignmentService {

    ProjectAssignmentResponse assignEmployeeToProject(AssignEmployeeRequest request);
    ProjectAssignmentResponse closeProjectAssignment(Long assignmentId);
    List<EmployeeProjectAssignmentResponse> getAssignmentsByProject(Long projectId);
    List<ProjectEmployeeAssignmentResponse> getAssignmentsByEmployee(Long employeeId);

}
