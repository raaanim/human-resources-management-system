package aitho.ranim.hrms.dto.projectAssignmentDto;

import aitho.ranim.hrms.enums.AssignmentRole;

import java.time.LocalDate;

public record EmployeeProjectAssignmentResponse(
        Long employeeId,
        String employeeFullName,
        AssignmentRole role,
        LocalDate startDate,
        LocalDate endDate
) {
}
