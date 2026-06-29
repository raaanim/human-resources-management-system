package aitho.ranim.hrms.dto.contractDto;

import aitho.ranim.hrms.enums.ContractType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(
        name = "ContractSummaryResponse",
        description = "Lightweight contract summary response"
)
public record ContractSummaryResponse(
        @Schema(description = "Contract ID", example = "1")
        long id,

        @Schema(description = "Contract type", example = "FULL_TIME")
        ContractType contractType,

        @Schema(description = "Job position", example = "Backend Developer")
        String position,

        @Schema(description = "Start date", example = "2026-01-01")
        LocalDate startDate,

        @Schema(description = "End date", example = "2027-01-01")
        LocalDate endDate,

        @Schema(description = "Active status", example = "true")
        boolean active,

        @Schema(description = "Monthly leave days", example = "2.5")
        BigDecimal monthlyLeaveDays,

        @Schema(description = "Monthly leave hours", example = "16")
        BigDecimal monthlyLeaveHours
) {
}
