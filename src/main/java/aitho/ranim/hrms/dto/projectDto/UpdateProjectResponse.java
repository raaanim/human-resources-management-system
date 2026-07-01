package aitho.ranim.hrms.dto.projectDto;

import java.time.LocalDate;

public record UpdateProjectResponse(
        LocalDate dateOfUpdate,
        String message
) {
}
