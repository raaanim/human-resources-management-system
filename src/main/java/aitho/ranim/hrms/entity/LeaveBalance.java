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
    @OneToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal accruedDays = BigDecimal.ZERO;
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal usedDays = BigDecimal.ZERO;
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal pendingDays  = BigDecimal.ZERO;
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal accruedHours  = BigDecimal.ZERO;
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal usedHours  = BigDecimal.ZERO;
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal pendingHours = BigDecimal.ZERO;
    private LocalDate lastAccrualDate;
}
