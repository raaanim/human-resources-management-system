package aitho.ranim.hrms.dto;

import java.time.LocalDate;

public record UpdateEmployeeResponse(
        LocalDate dateOfUpdate,
        String message
) {
}
