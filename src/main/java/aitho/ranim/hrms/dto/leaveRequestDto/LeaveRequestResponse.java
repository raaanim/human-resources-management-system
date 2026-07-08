package aitho.ranim.hrms.dto.leaveRequestDto;

import aitho.ranim.hrms.enums.LeaveRequestsStatus;
import aitho.ranim.hrms.enums.LeaveType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LeaveRequestResponse(
          Long employeeId,
          String employeeName,
          LeaveType leaveType,
          LocalDate startDate,
          LocalDate endDate,
          BigDecimal totalDays,
          LeaveRequestsStatus status,
          String employeeNotes,
          String reviewerNotes,
          Long reviewerId,
          String reviewerName,
          LocalDate requestedAt,
          LocalDate reviewedAt
) {
}
