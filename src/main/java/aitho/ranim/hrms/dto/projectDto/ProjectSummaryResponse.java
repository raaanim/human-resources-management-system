package aitho.ranim.hrms.dto.projectDto;

import aitho.ranim.hrms.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ProjectSummaryResponse",
        description = "Lightweight response containing essential project information"
)
public record ProjectSummaryResponse(

        @Schema(description = "Project ID", example = "10")
        Long id,

        @Schema(description = "Project name", example = "HRMS Platform")
        String name,

        @Schema(description = "Client name", example = "ACME Corp")
        String clientName,

        @Schema(description = "Project status", example = "IN_PROGRESS")
        ProjectStatus status
) {
}