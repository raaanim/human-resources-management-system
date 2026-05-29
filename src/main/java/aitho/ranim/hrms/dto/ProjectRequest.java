package aitho.ranim.hrms.dto;

import aitho.ranim.hrms.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;


public record ProjectRequest(
        @NotBlank(message = "Project name is required")
        String name,
        String description,
        @NotBlank
        String clientName,
        @NotBlank
        LocalDate startDate,
        @NotBlank
        ProjectStatus status,
        LocalDate endDate,
        BigDecimal budget
) {
}
