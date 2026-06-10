package aitho.ranim.hrms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjectResponse(
        String name,
        String description,
        String clientName,
        LocalDate startDate,
        aitho.ranim.hrms.enums.ProjectStatus status,
        LocalDate endDate,
        BigDecimal budget
) {
}
