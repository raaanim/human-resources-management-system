package aitho.ranim.hrms.utils;

import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveBalance;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class LeaveBalanceUtils {
    public LeaveBalance createLeaveBalanceForEmployee(Employee employee) {
        LeaveBalance leaveBalance = new LeaveBalance();
        leaveBalance.setEmployee(employee);
        leaveBalance.setAccruedDays(BigDecimal.ZERO);
        leaveBalance.setUsedDays(BigDecimal.ZERO);
        leaveBalance.setPendingDays(BigDecimal.ZERO);
        leaveBalance.setAccruedHours(BigDecimal.ZERO);
        leaveBalance.setUsedHours(BigDecimal.ZERO);
        leaveBalance.setPendingHours(BigDecimal.ZERO);
        return leaveBalance;
    }
}
