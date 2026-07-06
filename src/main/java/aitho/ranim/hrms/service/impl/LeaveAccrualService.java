package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveAccrualLogResponse;
import aitho.ranim.hrms.dto.leaveBalanceDto.LeaveAccrualProcessResponse;
import aitho.ranim.hrms.entity.Contract;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.repository.IContractRepository;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.ILeaveAccrualLogRepository;
import aitho.ranim.hrms.service.ILeaveAccrualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveAccrualService implements ILeaveAccrualService {
    private final IEmployeeRepository employeeRepository;
    private final IContractRepository contractRepository;
    private final ILeaveAccrualLogRepository leaveAccrualLogRepository;
    private final LeaveAccrualEmployeeService leaveAccrualEmployeeService;

    @Override
    public LeaveAccrualProcessResponse processMonthlyAccrual() {
        int processed = 0;
        int skipped = 0;
        int missing = 0;

        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        List<Employee> employees = employeeRepository.findByStatus("ACTIVE");

        for (Employee employee : employees) {

            try {
                Optional<Contract> contractOpt =
                        contractRepository.findFirstByEmployeeIdAndActiveTrue(employee.getId());

                if (contractOpt.isEmpty()) {
                    log.warn("Employee {} has no active contract", employee.getId());
                    missing++;
                    continue;
                }

                boolean alreadyProcessed =
                        leaveAccrualLogRepository.existsByEmployeeAndAccrualMonthAndAccrualYear(
                                employee, month, year);

                if (alreadyProcessed) {
                    log.info("Skipping employee {} because accrual already exists for {}/{}",
                            employee.getId(), month, year);
                    skipped++;
                    continue;
                }

                Contract contract = contractOpt.get();

                leaveAccrualEmployeeService.processAccrual(employee, contract, month, year, today);

                processed++;

            } catch (Exception ex) {
                log.error("Error processing employee {}", employee.getId(), ex);
            }
        }
        return new LeaveAccrualProcessResponse(processed, skipped, missing);
    }

    public List<LeaveAccrualLogResponse> getEmployeeLogs(Long employeeId) {
        return leaveAccrualLogRepository
                .findByEmployeeIdOrderByAccrualYearDescAccrualMonthDesc(employeeId)
                .stream()
                .map(log -> new LeaveAccrualLogResponse(
                        log.getAccrualMonth(),
                        log.getAccrualYear(),
                        log.getDaysAccrued(),
                        log.getHoursAccrued(),
                        log.getContractSnapshot(),
                        log.getProcessedAt()
                ))
                .toList();
    }

    public void processFirstMonthAccrual(Employee employee) {
        Optional<Contract> contractOpt =
                contractRepository.findFirstByEmployeeIdAndActiveTrue(employee.getId());

        if (contractOpt.isEmpty()) {
           return;
        }

        Contract contract = contractOpt.get();
        LocalDate startDate = contract.getStartDate();

        if (startDate == null) {
            log.warn("Employee {} has active contract without startDate", employee.getId());
            return;
        }

        int month = startDate.getMonthValue();
        int year = startDate.getYear();

        boolean alreadyProcessed =
                leaveAccrualLogRepository.existsByEmployeeAndAccrualMonthAndAccrualYear(
                        employee, month, year);

        if (alreadyProcessed) {
            log.info("First month accrual already exists for employee {} in {}/{}",
                    employee.getId(), month, year);
            return;
        }

        int totalWorkDays = countWorkingDaysInMonth(startDate);
        int remainingWorkDays = countWorkingDaysFrom(startDate);

        if (totalWorkDays == 0 || remainingWorkDays == 0) {
            log.warn("No working days available for first month accrual of employee {}", employee.getId());
            return;
        }

        BigDecimal monthlyDays = contract.getMonthlyLeaveDays() != null
                ? contract.getMonthlyLeaveDays()
                : BigDecimal.ZERO;

        BigDecimal monthlyHours = contract.getMonthlyLeaveHours() != null
                ? contract.getMonthlyLeaveHours()
                : BigDecimal.ZERO;

        BigDecimal ratio = BigDecimal.valueOf(remainingWorkDays)
                .divide(BigDecimal.valueOf(totalWorkDays), 4, RoundingMode.HALF_UP);

        BigDecimal firstMonthDays = monthlyDays.multiply(ratio).setScale(2, RoundingMode.HALF_UP);
        BigDecimal firstMonthHours = monthlyHours.multiply(ratio).setScale(2, RoundingMode.HALF_UP);

        leaveAccrualEmployeeService.processCustomAccrual(
                employee,
                contract,
                month,
                year,
                startDate,
                firstMonthDays,
                firstMonthHours
        );
    }

    private int countWorkingDaysInMonth(LocalDate date) {
        YearMonth yearMonth = YearMonth.from(date);
        int count = 0;

        for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
            LocalDate current = yearMonth.atDay(i);
            if (isWorkingDay(current)) {
                count++;
            }
        }
        return count;
    }

    private int countWorkingDaysFrom(LocalDate startDate) {
        YearMonth yearMonth = YearMonth.from(startDate);
        int count = 0;

        for (int i = startDate.getDayOfMonth(); i <= yearMonth.lengthOfMonth(); i++) {
            LocalDate current = yearMonth.atDay(i);
            if (isWorkingDay(current)) {
                count++;
            }
        }
        return count;
    }

    private boolean isWorkingDay(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }
}