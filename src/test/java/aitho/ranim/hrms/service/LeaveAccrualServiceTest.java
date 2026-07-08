package aitho.ranim.hrms.service;

import aitho.ranim.hrms.entity.Contract;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.repository.IContractRepository;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.ILeaveAccrualLogRepository;
import aitho.ranim.hrms.service.impl.LeaveAccrualEmployeeService;
import aitho.ranim.hrms.service.impl.LeaveAccrualService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class LeaveAccrualServiceTest {
    @Mock
    private IEmployeeRepository employeeRepository;

    @Mock
    private IContractRepository contractRepository;

    @Mock
    private ILeaveAccrualLogRepository leaveAccrualLogRepository;

    @Mock
    private LeaveAccrualEmployeeService leaveAccrualEmployeeService;

    @InjectMocks
    private LeaveAccrualService leaveAccrualService;

    @Test
    void shouldProcessFullQuotaWhenEmployeeStartsOnFirstDayOfMonth() {
        Employee employee = new Employee();
        employee.setId(1L);

        Contract contract = new Contract();
        contract.setStartDate(LocalDate.of(2026, 7, 1));
        contract.setMonthlyLeaveDays(new BigDecimal("2.17"));
        contract.setMonthlyLeaveHours(new BigDecimal("18.00"));

        when(contractRepository.findFirstByEmployeeIdAndActiveTrue(1L))
                .thenReturn(Optional.of(contract));
        when(leaveAccrualLogRepository.existsByEmployeeAndAccrualMonthAndAccrualYear(employee, 7, 2026))
                .thenReturn(false);

        leaveAccrualService.processFirstMonthAccrual(employee);

        verify(leaveAccrualEmployeeService).processCustomAccrual(
                eq(employee),
                eq(contract),
                eq(7),
                eq(2026),
                eq(LocalDate.of(2026, 7, 1)),
                eq(new BigDecimal("2.17")),
                eq(new BigDecimal("18.00"))
        );
    }

    @Test
    void shouldProcessMinimumQuotaWhenEmployeeStartsOnLastWorkingDay() {
        Employee employee = new Employee();
        employee.setId(1L);

        Contract contract = new Contract();
        contract.setStartDate(LocalDate.of(2026, 7, 31));
        contract.setMonthlyLeaveDays(new BigDecimal("2.17"));
        contract.setMonthlyLeaveHours(new BigDecimal("18.00"));

        when(contractRepository.findFirstByEmployeeIdAndActiveTrue(1L))
                .thenReturn(Optional.of(contract));
        when(leaveAccrualLogRepository.existsByEmployeeAndAccrualMonthAndAccrualYear(employee, 7, 2026))
                .thenReturn(false);

        leaveAccrualService.processFirstMonthAccrual(employee);

        verify(leaveAccrualEmployeeService).processCustomAccrual(
                eq(employee),
                eq(contract),
                eq(7),
                eq(2026),
                eq(LocalDate.of(2026, 7, 31)),
                eq(new BigDecimal("0.09")),
                eq(new BigDecimal("0.78"))
        );
    }
}
