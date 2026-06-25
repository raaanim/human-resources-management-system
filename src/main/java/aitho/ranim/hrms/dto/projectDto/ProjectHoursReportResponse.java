package aitho.ranim.hrms.dto.projectDto;

import aitho.ranim.hrms.dto.timeEntryDto.TimeEntryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Schema(
        name = "ProjectHoursReportResponse",
        description = "Report of total hours worked on a project including all time entries"
)
public record ProjectHoursReportResponse(

        @Schema(description = "Project ID", example = "10")
        Long projectId,

        @Schema(description = "Project name", example = "HRMS Platform")
        String projectName,

        @Schema(description = "Total hours logged for the project", example = "125.5")
        BigDecimal totalHours,

        @Schema(description = "List of time entries associated with the project")
        List<TimeEntryResponse> entries
) {
}
