package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.entity.Contract;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveAccrualLog;
import aitho.ranim.hrms.entity.LeaveBalance;
import aitho.ranim.hrms.repository.ILeaveAccrualLogRepository;
import aitho.ranim.hrms.repository.ILeaveBalanceRepository;
import aitho.ranim.hrms.utils.LeaveBalanceUtils;
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

        processCustomAccrual(employee, contract, month, year, today, monthlyDays, monthlyHours);
    }

    @Transactional
    public void processCustomAccrual(
            Employee employee,
            Contract contract,
            int month,
            int year,
            LocalDate accrualDate,
            BigDecimal daysToAdd,
            BigDecimal hoursToAdd
    ) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployee_Id(employee.getId())
                .orElseGet(() -> LeaveBalanceUtils.createLeaveBalanceForEmployee(employee));

        log.info("Before accrual employee={}, balanceId={}, days={}, hours={}",
                employee.getId(),
                balance.getId(),
                balance.getAccruedDays(),
                balance.getAccruedHours());

        balance.setAccruedDays(balance.getAccruedDays().add(daysToAdd));
        balance.setAccruedHours(balance.getAccruedHours().add(hoursToAdd));
        balance.setLastAccrualDate(accrualDate);

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
                .daysAccrued(daysToAdd)
                .hoursAccrued(hoursToAdd)
                .contractSnapshot(contract.getPosition())
                .processedAt(LocalDateTime.now())
                .build();

        leaveAccrualLogRepository.saveAndFlush(logEntry);
    }
}