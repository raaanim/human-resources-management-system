package aitho.ranim.hrms.dto.leaveBalanceDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LeaveAccrualLogResponse(
        int accrualMonth,
        int accrualYear,
        BigDecimal daysAccrued,
        BigDecimal hoursAccrued,
        String contractSnapshot,
        LocalDateTime processedAt
) {
}
