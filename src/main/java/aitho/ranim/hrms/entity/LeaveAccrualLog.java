package aitho.ranim.hrms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Table(
        name = "leave_accrual_log",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "leave_accrual_employee_month_year",
                        columnNames = {"employee_id", "accrual_month", "accrual_year"}
                )
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class LeaveAccrualLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Employee employee;
    private int accrualMonth;
    private int accrualYear;
    private BigDecimal daysAccrued;
    private BigDecimal hoursAccrued;
    private String contractSnapshot;
    private LocalDateTime processedAt;
}
