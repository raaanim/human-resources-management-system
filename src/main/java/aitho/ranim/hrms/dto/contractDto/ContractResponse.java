package aitho.ranim.hrms.dto.contractDto;
import aitho.ranim.hrms.dto.employeeDto.EmployeeDetailResponse;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.enums.ContractType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(
        name = "ContractResponse",
        description = "Full contract details response"
)
public record ContractResponse(
        @Schema(description = "Contract ID", example = "1")
        long id,
        @Schema(description = "Employee linked to contract")
        Employee employee,
        @Schema(description = "Type of contract", example = "FULL_TIME")
        ContractType contractType,
        @Schema(description = "Job position", example = "Software Engineer")
        String position,
        @Schema(description = "Department name", example = "IT")
        String department,

        @Schema(description = "Gross annual salary", example = "50000")
        BigDecimal grossAnnualSalary,

        @Schema(description = "Contract start date", example = "2026-01-01")
        LocalDate startDate,

        @Schema(description = "Contract end date", example = "2027-01-01")
        LocalDate endDate,

        @Schema(description = "Is contract active", example = "true")
        boolean active,

        @Schema(description = "Monthly leave days", example = "2.5")
        BigDecimal monthlyLeaveDays,

        @Schema(description = "Monthly leave hours", example = "20")
        BigDecimal monthlyLeaveHours,

        @Schema(description = "Additional notes", example = "Remote allowed")
        String notes,

        @Schema(description = "Creation date", example = "2026-01-01")
        LocalDate createdAt,

        @Schema(description = "Annual leave days", example = "30")
        BigDecimal annualLeaveDays
) {
}
