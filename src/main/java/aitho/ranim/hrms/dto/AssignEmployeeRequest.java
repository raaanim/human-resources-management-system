package aitho.ranim.hrms.dto;

import aitho.ranim.hrms.enums.AssignmentRole;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;

public record AssignEmployeeRequest(
        @NotNull
        Long employeeId,
        @NotNull
        Long projectId,
        @NotNull
        AssignmentRole role,
        @NotNull
        LocalDate startDate
) {
}
