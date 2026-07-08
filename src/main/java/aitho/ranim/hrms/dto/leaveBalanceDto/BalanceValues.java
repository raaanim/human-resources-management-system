package aitho.ranim.hrms.dto.leaveBalanceDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceValues(
        BigDecimal accruedDays,
        BigDecimal usedDays,
        BigDecimal pendingDays,
        BigDecimal availableDays,
        BigDecimal accruedHours,
        BigDecimal usedHours,
        BigDecimal pendingHours,
        BigDecimal availableHours,
        LocalDate lastAccrualDate
) {
}
