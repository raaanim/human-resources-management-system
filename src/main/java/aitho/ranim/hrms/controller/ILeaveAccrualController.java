package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveAccrualLogResponse;
import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveAccrualProcessResponse;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

public interface ILeaveAccrualController {
    LeaveAccrualProcessResponse processNow();
    List<LeaveAccrualLogResponse> getLogs(@PathVariable Long employeeId);
}
