package aitho.ranim.hrms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;


@Table(name = "leave_balance")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id", unique = true)
    private Employee employee;
    private BigDecimal accruedDays;
    private BigDecimal usedDays;
    private BigDecimal pendingDays;
    private BigDecimal accruedHours;
    private BigDecimal usedHours;
    private BigDecimal pendingHours;
    private LocalDate lastAccrualDate;
}
