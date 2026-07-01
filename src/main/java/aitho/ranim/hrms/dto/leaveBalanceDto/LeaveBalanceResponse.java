package aitho.ranim.hrms.dto.leaveBalanceDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LeaveBalanceResponse(
        BigDecimal accruedDays,
        BigDecimal usedDays,
        BigDecimal pendingDays,
        BigDecimal accruedHours,
        BigDecimal usedHours,
        BigDecimal pendingHours,
        LocalDate lastAccrualDate
) {
    public BigDecimal availableDays() {
        return accruedDays
                .subtract(usedDays)
                .subtract(pendingDays);
    }
}
