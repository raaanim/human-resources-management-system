package aitho.ranim.hrms.utils;

import aitho.ranim.hrms.dto.projectAssignmentDto.EmployeeProjectAssignmentResponse;
import aitho.ranim.hrms.dto.projectAssignmentDto.ProjectAssignmentResponse;
import aitho.ranim.hrms.dto.projectAssignmentDto.ProjectEmployeeAssignmentResponse;
import aitho.ranim.hrms.entity.ProjectAssignment;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProjectAssignmentUtils {

    public ProjectAssignmentResponse toResponse(ProjectAssignment assignment) {
        return new ProjectAssignmentResponse(
                assignment.getId(),
                assignment.getEmployee().getId(),
                assignment.getEmployee().getFirstName() + " " + assignment.getEmployee().getLastName(),
                assignment.getProject().getId(),
                assignment.getProject().getName(),
                assignment.getRole(),
                assignment.getStartDate(),
                assignment.getEndDate()
        );
    }

    public EmployeeProjectAssignmentResponse toEmployeeResponse(ProjectAssignment a) {
        return new EmployeeProjectAssignmentResponse(
                a.getEmployee().getId(),
                a.getEmployee().getFirstName() + " " + a.getEmployee().getLastName(),
                a.getRole(),
                a.getStartDate(),
                a.getEndDate()
        );
    }

    public ProjectEmployeeAssignmentResponse toProjectResponse(ProjectAssignment a) {
        return new ProjectEmployeeAssignmentResponse(
                a.getProject().getId(),
                a.getProject().getName(),
                a.getRole(),
                a.getStartDate(),
                a.getEndDate()
        );
    }
}
