package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.entity.Contract;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveAccrualLog;
import aitho.ranim.hrms.entity.LeaveBalance;
import aitho.ranim.hrms.repository.ILeaveAccrualLogRepository;
import aitho.ranim.hrms.repository.ILeaveBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
    public class LeaveAccrualEmployeeService {
    private final ILeaveBalanceRepository leaveBalanceRepository;
    private final ILeaveAccrualLogRepository leaveAccrualLogRepository;

    @Transactional
    public void processAccrual(
            Employee employee,
            Contract contract,
            int month,
            int year,
            LocalDate today
    ) {

        LeaveBalance balance = leaveBalanceRepository.findByEmployee(employee)
                .orElseGet(() -> {
                    LeaveBalance b = new LeaveBalance();
                    b.setEmployee(employee);
                    b.setAccruedDays(BigDecimal.ZERO);
                    b.setUsedDays(BigDecimal.ZERO);
                    b.setPendingDays(BigDecimal.ZERO);
                    b.setAccruedHours(BigDecimal.ZERO);
                    b.setUsedHours(BigDecimal.ZERO);
                    b.setPendingHours(BigDecimal.ZERO);
                    return b;
                });

        balance.setAccruedDays(
                safe(balance.getAccruedDays()).add(contract.getMonthlyLeaveDays())
        );

        balance.setAccruedHours(
                safe(balance.getAccruedHours()).add(contract.getMonthlyLeaveHours())
        );

        balance.setLastAccrualDate(today);

        leaveBalanceRepository.save(balance);

        LeaveAccrualLog logEntry = LeaveAccrualLog.builder()
                .employee(employee)
                .accrualMonth(month)
                .accrualYear(year)
                .daysAccrued(contract.getMonthlyLeaveDays())
                .hoursAccrued(contract.getMonthlyLeaveHours())
                .contractSnapshot(contract.getPosition())
                .processedAt(LocalDateTime.now())
                .build();

        leaveAccrualLogRepository.save(logEntry);
    }

    private BigDecimal safe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
