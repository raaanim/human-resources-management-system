package aitho.ranim.hrms.dto.employeeDto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(
        name = "EmployeeDetailResponse",
        description = "Detailed employee information response"
)
public record EmployeeDetailResponse(

        @Schema(description = "Employee ID", example = "1")
        Long id,

        @Schema(description = "First name", example = "Mario")
        String firstName,

        @Schema(description = "Last name", example = "Rossi")
        String lastName,

        @Schema(description = "Date of birth", example = "1990-05-20")
        LocalDate dateOfBirth,

        @Schema(description = "Gender", example = "M")
        String gender,

        @Schema(description = "Nationality", example = "Italian")
        String nationality,

        @Schema(description = "Birth place", example = "Rome")
        String birthPlace,

        @Schema(description = "Phone number", example = "+39 333 1234567")
        String phoneNumber,

        @Schema(description = "Street address", example = "Via Roma 10")
        String address,

        @Schema(description = "City", example = "Rome")
        String city,

        @Schema(description = "Province", example = "RM")
        String province,

        @Schema(description = "Postal code", example = "00100")
        String postalCode,

        @Schema(description = "Country", example = "Italy")
        String country,

        @Schema(description = "Work location", example = "Rome HQ")
        String workLocation
) {
}
