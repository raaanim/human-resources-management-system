package aitho.ranim.hrms.entity;

import aitho.ranim.hrms.enums.ContractType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "contracts")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Employee employee;
    @Enumerated(EnumType.STRING)
    private ContractType contractType;
    private String position;
    private String department;
    private BigDecimal grossAnnualSalary;
    @NotNull
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    @DecimalMin("0.0")
    private BigDecimal monthlyLeaveDays;
   @DecimalMin("0.0")
    private BigDecimal monthlyLeaveHours;
    private String notes;
    private LocalDate createdAt;
}
