package aitho.ranim.hrms.dto.employeeDto;

import java.time.LocalDate;

public record UpdateEmployeeResponse(
        LocalDate dateOfUpdate,
        String message
) {
}
