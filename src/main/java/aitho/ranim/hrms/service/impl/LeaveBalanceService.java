package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.leaveBalanceDto.BalanceValues;
import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveBalanceResponse;
import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveBalanceSummaryResponse;
import aitho.ranim.hrms.entity.Contract;
import aitho.ranim.hrms.entity.LeaveBalance;
import aitho.ranim.hrms.exception.ContractException;
import aitho.ranim.hrms.exception.LeaveBalanceException;
import aitho.ranim.hrms.repository.IContractRepository;
import aitho.ranim.hrms.repository.ILeaveBalanceRepository;
import aitho.ranim.hrms.service.ILeaveBalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;


@Service
@Slf4j
@RequiredArgsConstructor
public class LeaveBalanceService implements ILeaveBalanceService {
    private final ILeaveBalanceRepository leaveBalanceRepository;
    private final IContractRepository contractRepository;

    public LeaveBalanceResponse getEmployeeLeaveBalance(Long employeeId) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployee_Id(employeeId)
                .orElseThrow(() -> new LeaveBalanceException(
                        "Leave balance not found for employee id: " + employeeId,
                        HttpStatus.NOT_FOUND,
                        "/leave-balance"));
        BalanceValues value = getBalanceValues(balance);

        return new LeaveBalanceResponse(
                value.accruedDays(),
                value.usedDays(),
                value.pendingDays(),
                value.availableDays(),
                value.accruedHours(),
                value.usedHours(),
                value.pendingHours(),
                value.availableHours(),
                value.lastAccrualDate()

        );
    }


    public LeaveBalanceSummaryResponse getEmployeeLeaveBalanceSummary(Long employeeId) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployee_Id(employeeId)
                .orElseThrow(() -> new LeaveBalanceException(
                        "Leave balance not found for employee id: " + employeeId, HttpStatus.NOT_FOUND,"/leave-balance"));

        Contract contract = contractRepository.findFirstByEmployeeIdAndActiveTrue(employeeId)
                .orElseThrow(() -> new ContractException(
                        "Active contract not found for employee id: " + employeeId, HttpStatus.NOT_FOUND, "/contract"));

        BalanceValues value = getBalanceValues(balance);

        BigDecimal nextMonthExpectedDays = safe(contract.getMonthlyLeaveDays());
        BigDecimal nextMonthExpectedHours = safe(contract.getMonthlyLeaveHours());

        return new LeaveBalanceSummaryResponse(
                value.accruedDays(),
                value.usedDays(),
                value.pendingDays(),
                value.availableDays(),
                value.accruedHours(),
                value.usedHours(),
                value.pendingHours(),
                value.availableHours(),
                value.lastAccrualDate(),
                nextMonthExpectedDays,
                nextMonthExpectedHours
        );
    }

    private BalanceValues getBalanceValues(LeaveBalance balance) {
        BigDecimal accruedDays = safe(balance.getAccruedDays());
        BigDecimal usedDays = safe(balance.getUsedDays());
        BigDecimal pendingDays = safe(balance.getPendingDays());
        BigDecimal availableDays = accruedDays.subtract(usedDays).subtract(pendingDays);

        BigDecimal accruedHours = safe(balance.getAccruedHours());
        BigDecimal usedHours = safe(balance.getUsedHours());
        BigDecimal pendingHours = safe(balance.getPendingHours());
        BigDecimal availableHours = accruedHours.subtract(usedHours).subtract(pendingHours);

        return new BalanceValues(
                accruedDays,
                usedDays,
                pendingDays,
                availableDays,
                accruedHours,
                usedHours,
                pendingHours,
                availableHours,
                balance.getLastAccrualDate()
        );
    }
    private BigDecimal safe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
