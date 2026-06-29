package aitho.ranim.hrms.dto.contractDto;

import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.enums.ContractType;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContractRequest(
          Employee employee,
          ContractType contractType,
          String position,
          String department,
          BigDecimal grossAnnualSalary,
          LocalDate startDate,
          LocalDate endDate,
          @DecimalMin("0.0")
          BigDecimal monthlyLeaveDays,
          @DecimalMin("0.0")
          BigDecimal monthlyLeaveHours,
          String notes
) {
}
