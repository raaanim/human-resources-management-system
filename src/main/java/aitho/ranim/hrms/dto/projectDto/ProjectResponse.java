package aitho.ranim.hrms.dto.projectDto;

import aitho.ranim.hrms.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(
        name = "ProjectResponse",
        description = "Full project details response"
)
public record ProjectResponse(

        @Schema(description = "Project name", example = "HRMS Platform")
        String name,

        @Schema(description = "Project description", example = "Internal HR management system")
        String description,

        @Schema(description = "Client name", example = "ACME Corp")
        String clientName,

        @Schema(description = "Project start date", example = "2025-01-01")
        LocalDate startDate,

        @Schema(description = "Project status", example = "IN_PROGRESS")
        ProjectStatus status,

        @Schema(description = "Project end date", example = "2026-12-31")
        LocalDate endDate,

        @Schema(description = "Project budget", example = "50000")
        BigDecimal budget
) {
}