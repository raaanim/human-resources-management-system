package aitho.ranim.hrms.dto;

import java.time.LocalDate;

public record ActivateEmployeeResponse(
        LocalDate dateOfCreation,
        String message

) {
}
