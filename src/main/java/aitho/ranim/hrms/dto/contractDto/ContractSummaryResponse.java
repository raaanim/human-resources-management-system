package aitho.ranim.hrms.dto.contractDto;

import aitho.ranim.hrms.enums.ContractType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContractSummaryResponse(
        long id,
        ContractType contractType,
        String position,
        LocalDate startDate,
        LocalDate endDate,
        boolean active,
        BigDecimal monthlyLeaveDays,
        BigDecimal monthlyLeaveHours
) {
}
