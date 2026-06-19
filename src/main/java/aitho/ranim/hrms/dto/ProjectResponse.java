package aitho.ranim.hrms.dto;

import aitho.ranim.hrms.enums.ProjectStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjectResponse(
        String name,
        String description,
        String clientName,
        LocalDate startDate,
        ProjectStatus status,
        LocalDate endDate,
        BigDecimal budget
) {
}
