package aitho.ranim.hrms.dto;

import aitho.ranim.hrms.enums.ProjectStatus;
import aitho.ranim.hrms.validation.ValidProjectDates;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;


@ValidProjectDates
public record ProjectRequest(
        @NotBlank(message = "Project name is required")
        String name,
        String description,
        @NotBlank(message = "Client name is required")
        String clientName,
        LocalDate startDate,
        ProjectStatus status,
        LocalDate endDate,
        BigDecimal budget
) {
}
