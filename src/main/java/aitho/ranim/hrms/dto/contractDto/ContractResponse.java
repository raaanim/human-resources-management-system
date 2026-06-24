package aitho.ranim.hrms.dto.contractDto;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.enums.ContractType;
import java.math.BigDecimal;
import java.time.LocalDate;


public record ContractResponse(
           long id,
           Employee employee,
           ContractType contractType,
           String position,
           String department,
           BigDecimal grossAnnualSalary,
           LocalDate startDate,
           LocalDate endDate,
           boolean active,
           BigDecimal monthlyLeaveDays,
           BigDecimal monthlyLeaveHours,
           String notes,
           LocalDate createdAt,
           BigDecimal annualLeaveDays
) {
}
