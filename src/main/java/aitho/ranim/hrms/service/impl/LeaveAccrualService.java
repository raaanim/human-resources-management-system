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
import java.time.LocalDate;
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
}
