package aitho.ranim.hrms.dto.leaveRequestDto;

import aitho.ranim.hrms.enums.LeaveType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LeaveRequestSubmitRequest(
        LeaveType leaveType,
        @NotNull(message = "Start date cannot be null")
        LocalDate startDate,
        @NotNull(message = "End date cannot be null")
        LocalDate endDate,
        String employeeNotes
) {
}
