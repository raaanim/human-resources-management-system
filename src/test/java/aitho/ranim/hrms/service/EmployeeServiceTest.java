package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.ActivateAccountRequest;
import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.service.impl.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private IEmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void testCreateEmployee() {
        EmployeeRequest request = new EmployeeRequest(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "Male",
                "American",
                "New York",
                "+1 212-345-6789",
                "johndoe@email.com",
                "123John$$Doe",
                "johndoepersonal@email.com",
                "350 5th Avenue, New York, NY 10118",
                "New York",
                "USA",
                "10118",
                "NY",
                "New York"
        );
        when(passwordEncoder.encode("123John$$Doe"))
                .thenReturn("encodedPassword");

        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = employeeService.createEmployee(request);

        assertNotNull(response);

        verify(passwordEncoder, times(1)).encode("123John$$Doe");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }


    @Test
    public void testActivateEmployee() {
        String token = "valid-token";

        Employee employee = new Employee();
        employee.setActivationToken(token);
        employee.setStatus("PENDING");

        ActivateAccountRequest request = new ActivateAccountRequest("newPassword");

        when(employeeRepository.findByActivationToken(token))
                .thenReturn(Optional.of(employee));

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("encodedNewPassword");

        employeeService.activateEmployee(token, request);

        assertEquals("ACTIVE", employee.getStatus());
        assertNull(employee.getActivationToken());
        assertEquals("encodedNewPassword", employee.getPassword());

        verify(employeeRepository, times(1)).save(employee);

    }

    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {
        String token = "invalid-token";

        ActivateAccountRequest request = new ActivateAccountRequest("newPassword");

        when(employeeRepository.findByActivationToken(token))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            employeeService.activateEmployee(token, request);
        });

        verify(employeeRepository, never()).save(any());
    }

}
