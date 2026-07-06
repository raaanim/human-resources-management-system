package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveAccrualLogResponse;
import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveAccrualProcessResponse;
import aitho.ranim.hrms.entity.Employee;

import java.util.List;


public interface ILeaveAccrualService {
    LeaveAccrualProcessResponse processMonthlyAccrual();
    List<LeaveAccrualLogResponse> getEmployeeLogs(Long employeeId);
    void processFirstMonthAccrual(Employee employee);
}
