package aitho.ranim.hrms.dto.projectAssignmentDto;

import aitho.ranim.hrms.enums.AssignmentRole;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ProjectAssignmentResponse(
        Long id,
        Long employeeId,
        String employeeFullName,
        Long projectId,
        @NotBlank(message = "Project name is required")
        String projectName,
        AssignmentRole role,
        LocalDate startDate,
        LocalDate endDate
) {
}
