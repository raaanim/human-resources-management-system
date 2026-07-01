package aitho.ranim.hrms.dto.projectAssignmentDto;

import aitho.ranim.hrms.enums.AssignmentRole;

import java.time.LocalDate;

public record ProjectEmployeeAssignmentResponse(
        Long projectId,
        String projectName,
        AssignmentRole role,
        LocalDate startDate,
        LocalDate endDate
) {
}
