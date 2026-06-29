package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.contractDto.ContractRequest;
import aitho.ranim.hrms.dto.contractDto.ContractResponse;
import aitho.ranim.hrms.dto.contractDto.ContractSummaryResponse;
import aitho.ranim.hrms.entity.Contract;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.enums.ContractType;
import aitho.ranim.hrms.enums.RoleName;
import aitho.ranim.hrms.exception.ContractException;
import aitho.ranim.hrms.exception.EmployeeException;
import aitho.ranim.hrms.repository.IContractRepository;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.service.impl.ContractService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import java.util.Arrays;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

     @Mock
     IContractRepository contractRepository;
     @Mock
     IEmployeeRepository employeeRepository;
     @InjectMocks
     ContractService contractService;

     @Test
    void givenValidRequest_whenCreateContract_thenCreateContract()
    {
        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(contractRepository.save(any(Contract.class)))
                .thenAnswer(invocation ->{
                    Contract contract = invocation.getArgument(0);
                    contract.setId(1L);
                    return contract;
                });

        ContractRequest contractRequest = new ContractRequest(
                employee,
                ContractType.FREELANCE,
                "Designer",
                "UX Department",
                BigDecimal.valueOf(35000),
                LocalDate.of(2025,1,1),
                LocalDate.of(2026,1,1),
                BigDecimal.valueOf(2.5),
                BigDecimal.valueOf(20.0),
                "Contratto a tempo determinato"
        );
        ContractResponse contractResponse = contractService.createContract(contractRequest);
        assertNotNull(contractResponse);
        assertEquals(1L, contractResponse.id());
        assertEquals("Designer", contractResponse.position());
        assertEquals("UX Department", contractResponse.department());
        assertEquals(0, contractResponse.grossAnnualSalary()
                .compareTo(BigDecimal.valueOf(35000)));
        assertEquals(LocalDate.of(2025, 1, 1), contractResponse.startDate());
        assertEquals(LocalDate.of(2026, 1, 1), contractResponse.endDate());
        assertEquals(BigDecimal.valueOf(2.5), contractResponse.monthlyLeaveDays());
        assertEquals("Contratto a tempo determinato", contractResponse.notes());
        assertNotNull(contractResponse.annualLeaveDays());

        verify(employeeRepository).findById(1L);
        verify(contractRepository).save(any(Contract.class));
    }

    @Test
    void givenInvalidEmployee_whenCreateContract_thenThrowException()
    {
        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        Employee employee = new Employee();
        employee.setId(1L);

        ContractRequest request = new ContractRequest(
               employee,
                ContractType.FREELANCE,
                "Designer",
                "UX",
                BigDecimal.valueOf(35000),
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                BigDecimal.valueOf(2.5),
                BigDecimal.valueOf(20.0),
                "Contratto a tempo determinato"
        );

        assertThrows(EmployeeException.class,
                () -> contractService.createContract(request));

        verify(contractRepository, never()).save(any());
    }

    @Test
    void givenValidAuthentication_whenGetEmployeeContractDetails_thenReturnListOfContract()
    {
        Long employeeId = 1L;

        Contract contract = new Contract();
        contract.setId(10L);
        contract.setPosition("Developer");

        Contract contract1 = new Contract();
        contract1.setId(11L);
        contract1.setPosition("Designer");

        when(contractRepository.findByEmployeeIdOrderByStartDateDesc(employeeId))
                .thenReturn(Arrays.asList(contract, contract1));

        mockAuthAsAdmin();

        List<ContractSummaryResponse> result = contractService.getEmployeeContractDetails(employeeId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(contractRepository).findByEmployeeIdOrderByStartDateDesc(employeeId);
        verify(contractRepository, never()).save(any());
    }


    @Test
    void givenValidAdmin_whenGetActiveContract_thenReturnContract() {
        Long employeeId = 1L;

        mockAuthAsAdmin();

        Contract contract = new Contract();
        contract.setId(1L);
        contract.setActive(true);

        when(contractRepository.findFirstByEmployeeIdAndActiveTrue(employeeId))
                .thenReturn(Optional.of(contract));

        ContractResponse response = contractService.getActiveContract(employeeId);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertTrue(response.active());

        verify(contractRepository).findFirstByEmployeeIdAndActiveTrue(employeeId);
        verify(contractRepository, never()).save(any());
    }

    @Test
    void givenValidContract_whenUpdateContract_thenReturnUpdatedContract() {

        Long id = 1L;

        Contract contract = new Contract();
        contract.setId(id);

        ContractRequest request = new ContractRequest(
                new Employee(),
                ContractType.FREELANCE,
                "Updated Designer",
                "Updated UX Department",
                BigDecimal.valueOf(40000),
                LocalDate.of(2025, 2, 1),
                LocalDate.of(2026, 2, 1),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(22.0),
                "Updated notes"
        );

        when(contractRepository.findById(id))
                .thenReturn(Optional.of(contract));

        when(contractRepository.save(any(Contract.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ContractResponse response = contractService.updateContract(request, id);

        assertNotNull(response);
        assertEquals("Updated Designer", response.position());
        assertEquals("Updated UX Department", response.department());
        assertEquals("Updated notes", response.notes());

        verify(contractRepository).findById(id);
        verify(contractRepository).save(contract);
    }

    @Test
    void givenInvalidId_whenUpdateContract_thenThrowException() {

        Long id = 1L;

        ContractRequest request = mock(ContractRequest.class);

        when(contractRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ContractException.class,
                () -> contractService.updateContract(request, id));
    }

    @Test
    void givenValidContract_whenActivateContract_thenDeactivateOthersAndActivateCurrent() {

        Long id = 1L;
        Long employeeId = 10L;

        Contract contract = new Contract();
        contract.setId(id);

        Employee employee = new Employee();
        employee.setId(employeeId);
        contract.setEmployee(employee);

        when(contractRepository.findById(id))
                .thenReturn(Optional.of(contract));

        contractService.activateContract(id);

        assertTrue(contract.isActive());

        verify(contractRepository).findById(id);
        verify(contractRepository).deactivateByEmployeeId(employeeId);
        verify(contractRepository).save(contract);
    }

    @Test
    void givenNoActiveContract_whenGetActiveContract_thenThrowException() {

        Long employeeId = 1L;

        mockAuthAsAdmin();

        when(contractRepository.findFirstByEmployeeIdAndActiveTrue(employeeId))
                .thenReturn(Optional.empty());

        assertThrows(ContractException.class,
                () -> contractService.getActiveContract(employeeId));
    }

    private void mockAuthAsAdmin() {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(
                        "1",
                        null,
                        RoleName.ROLE_ADMIN.name()
                )
        );
    }
}
