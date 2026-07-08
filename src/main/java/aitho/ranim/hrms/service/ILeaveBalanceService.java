package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveBalanceResponse;
import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveBalanceSummaryResponse;

public interface ILeaveBalanceService {
    LeaveBalanceResponse getEmployeeLeaveBalance(Long employeeId);
    LeaveBalanceSummaryResponse getEmployeeLeaveBalanceSummary(Long employeeId);
}
