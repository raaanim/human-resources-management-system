package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.ILeaveAccrualController;
import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveAccrualLogResponse;
import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveAccrualProcessResponse;
import aitho.ranim.hrms.service.impl.LeaveAccrualService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leave-accrual")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class LeaveAccrualController implements ILeaveAccrualController {
    private final LeaveAccrualService leaveAccrualService;

    @PostMapping("/process-now")
    public LeaveAccrualProcessResponse processNow() {
        return leaveAccrualService.processMonthlyAccrual();
    }

    @GetMapping("/employee/{employeeId}/log")
    public List<LeaveAccrualLogResponse> getLogs(@PathVariable Long employeeId) {
        return leaveAccrualService.getEmployeeLogs(employeeId);
    }
}
