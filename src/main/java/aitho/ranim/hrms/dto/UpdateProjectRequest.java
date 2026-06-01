package aitho.ranim.hrms.dto;

import aitho.ranim.hrms.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record UpdateProjectRequest(
        @NotBlank(message = "Project name is required")
        String name,
        String description,
        @NotBlank(message = "Client name is required")
        String clientName,
        @NotBlank
        String startDate,
        ProjectStatus status,
        String endDate,
        BigDecimal budget
) {
}
