package aitho.ranim.hrms.dto;

import java.time.LocalDate;

public record DeleteEmployeeResponse(
        LocalDate dateOfDeletion,
        String message
) {
}
