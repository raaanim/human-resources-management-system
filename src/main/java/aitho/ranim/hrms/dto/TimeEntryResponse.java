package aitho.ranim.hrms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TimeEntryResponse(
        Long id,
        String employeeName,
        String projectName,
        Long projectId,
        LocalDate date,
        BigDecimal hoursWorked,
        String description,
        LocalDate createdAt
) {
}
