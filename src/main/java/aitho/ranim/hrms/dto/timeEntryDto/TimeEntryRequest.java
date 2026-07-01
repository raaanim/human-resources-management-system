package aitho.ranim.hrms.dto.timeEntryDto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TimeEntryRequest(
        Long projectId,
        LocalDate date,
        @DecimalMin(value = "0.5")
        @DecimalMax(value = "24.0")
        BigDecimal hoursWorked,
        String description
) {
}
