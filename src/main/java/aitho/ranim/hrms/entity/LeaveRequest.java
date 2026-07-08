package aitho.ranim.hrms.entity;

import aitho.ranim.hrms.enums.LeaveRequestsStatus;
import aitho.ranim.hrms.enums.LeaveType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "leave_request")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    Employee employee;
    LeaveType leaveType;
    LocalDate startDate;
    LocalDate endDate;
    BigDecimal totalDays;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    LeaveRequestsStatus status = LeaveRequestsStatus.PENDING;
    String employeeNotes;
    String reviewerNotes;
    @ManyToOne
    Employee reviewer;
    LocalDate requestedAt;
    LocalDate reviewedAt;
}
