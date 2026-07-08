package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.ILeaveBalanceController;
import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveBalanceResponse;
import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveBalanceSummaryResponse;
import aitho.ranim.hrms.service.ILeaveBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/leave-balance")
public class LeaveBalanceController implements ILeaveBalanceController {
    private final ILeaveBalanceService leaveBalanceService;

    public LeaveBalanceController(ILeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or #employeeId == authentication.principal.id")
    public ResponseEntity<LeaveBalanceResponse> getLeaveBalanceForEmployee(@PathVariable Long employeeId) {
        LeaveBalanceResponse response = leaveBalanceService.getEmployeeLeaveBalance(employeeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @GetMapping("/employee/{employeeId}/summary")
    public ResponseEntity<LeaveBalanceSummaryResponse> getLeaveBalanceSummaryForEmployee(@PathVariable Long employeeId) {
        LeaveBalanceSummaryResponse response = leaveBalanceService.getEmployeeLeaveBalanceSummary(employeeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
