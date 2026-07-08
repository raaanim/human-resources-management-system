package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveBalanceResponse;
import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveBalanceSummaryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface ILeaveBalanceController {
    ResponseEntity<LeaveBalanceResponse> getLeaveBalanceForEmployee(@PathVariable Long employeeId);
    ResponseEntity<LeaveBalanceSummaryResponse> getLeaveBalanceSummaryForEmployee(@PathVariable Long employeeId);
}
