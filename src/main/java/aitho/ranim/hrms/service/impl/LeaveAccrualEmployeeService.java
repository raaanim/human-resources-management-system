package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.entity.Contract;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveAccrualLog;
import aitho.ranim.hrms.entity.LeaveBalance;
import aitho.ranim.hrms.repository.ILeaveAccrualLogRepository;
import aitho.ranim.hrms.repository.ILeaveBalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
    public class LeaveAccrualEmployeeService {
    private final ILeaveBalanceRepository leaveBalanceRepository;
    private final ILeaveAccrualLogRepository leaveAccrualLogRepository;

    @Transactional
    public void processAccrual(Employee employee, Contract contract, int month, int year, LocalDate today) {

        BigDecimal monthlyDays = contract.getMonthlyLeaveDays() != null
                ? contract.getMonthlyLeaveDays()
                : BigDecimal.ZERO;

        BigDecimal monthlyHours = contract.getMonthlyLeaveHours() != null
                ? contract.getMonthlyLeaveHours()
                : BigDecimal.ZERO;

        LeaveBalance balance = leaveBalanceRepository.findByEmployee_Id(employee.getId())
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

        log.info("Before accrual employee={}, balanceId={}, days={}, hours={}",
                employee.getId(),
                balance.getId(),
                balance.getAccruedDays(),
                balance.getAccruedHours());

        balance.setAccruedDays(balance.getAccruedDays().add(monthlyDays));
        balance.setAccruedHours(balance.getAccruedHours().add(monthlyHours));
        balance.setLastAccrualDate(today);

        LeaveBalance savedBalance = leaveBalanceRepository.saveAndFlush(balance);

        log.info("After accrual employee={}, balanceId={}, days={}, hours={}",
                employee.getId(),
                savedBalance.getId(),
                savedBalance.getAccruedDays(),
                savedBalance.getAccruedHours());

        LeaveAccrualLog logEntry = LeaveAccrualLog.builder()
                .employee(employee)
                .accrualMonth(month)
                .accrualYear(year)
                .daysAccrued(monthlyDays)
                .hoursAccrued(monthlyHours)
                .contractSnapshot(contract.getPosition())
                .processedAt(LocalDateTime.now())
                .build();

        leaveAccrualLogRepository.saveAndFlush(logEntry);
    }
}