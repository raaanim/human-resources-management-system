package aitho.ranim.hrms.utils;

import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestResponse;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestSubmitRequest;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveRequest;
import aitho.ranim.hrms.enums.LeaveRequestsStatus;
import lombok.experimental.UtilityClass;
import java.math.BigDecimal;
import java.time.LocalDate;

@UtilityClass
public class LeaveRequestUtils {

    public LeaveRequest toSubmitLeaveRequest(LeaveRequestSubmitRequest request,
                                                      Employee employee,
                                                      BigDecimal totalDays) {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(request.leaveType());
        leaveRequest.setStartDate(request.startDate());
        leaveRequest.setEndDate(request.endDate());
        leaveRequest.setTotalDays(totalDays);
        leaveRequest.setStatus(LeaveRequestsStatus.PENDING);
        leaveRequest.setEmployeeNotes(request.employeeNotes());
        leaveRequest.setRequestedAt(LocalDate.now());
        return leaveRequest;
    }

    public LeaveRequestResponse toLeaveRequestResponse(LeaveRequest leaveRequest) {
        return new LeaveRequestResponse(
                leaveRequest.getEmployee().getId(),

                leaveRequest.getEmployee().getFirstName()
                        + " "
                        + leaveRequest.getEmployee().getLastName(),

                leaveRequest.getLeaveType(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                leaveRequest.getTotalDays(),
                leaveRequest.getStatus(),
                leaveRequest.getEmployeeNotes(),
                leaveRequest.getReviewerNotes(),

                leaveRequest.getReviewer() != null
                        ? leaveRequest.getReviewer().getId()
                        : null,

                leaveRequest.getReviewer() != null
                        ? leaveRequest.getReviewer().getFirstName()
                        + " "
                        + leaveRequest.getReviewer().getLastName()
                        : null,

                leaveRequest.getRequestedAt(),
                leaveRequest.getReviewedAt()
        );
    }
}
