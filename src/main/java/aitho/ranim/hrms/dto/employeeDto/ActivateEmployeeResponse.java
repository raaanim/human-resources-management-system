package aitho.ranim.hrms.dto.employeeDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(
        name = "ActivateEmployeeResponse",
        description = "Response returned when an employee is activated"
)
public record ActivateEmployeeResponse(
        @Schema(description = "Activation date", example = "2026-06-25")
        LocalDate dateOfCreation,

        @Schema(description = "Result message", example = "Employee activated successfully")
        String message
) {
}
