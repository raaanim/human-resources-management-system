package aitho.ranim.hrms.dto.timeEntryDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(
        name = "TimeEntryResponse",
        description = "Response representing a time entry logged by an employee"
)
public record TimeEntryResponse(

        @Schema(description = "Time entry ID", example = "100")
        Long id,

        @Schema(description = "Employee full name", example = "Mario Rossi")
        String employeeName,

        @Schema(description = "Project name", example = "HRMS Platform")
        String projectName,

        @Schema(description = "Project ID", example = "10")
        Long projectId,

        @Schema(description = "Date of the time entry", example = "2026-06-25")
        LocalDate date,

        @Schema(description = "Hours worked", example = "8.5")
        BigDecimal hoursWorked,

        @Schema(description = "Optional description of work done", example = "Implemented authentication module")
        String description,

        @Schema(description = "Creation date of the entry", example = "2026-06-25")
        LocalDate createdAt
) {
}