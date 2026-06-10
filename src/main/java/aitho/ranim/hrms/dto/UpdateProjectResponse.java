package aitho.ranim.hrms.dto;

import java.time.LocalDate;

public record UpdateProjectResponse(
        LocalDate dateOfUpdate,
        String message
) {
}
