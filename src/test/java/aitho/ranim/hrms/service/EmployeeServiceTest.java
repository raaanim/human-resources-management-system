package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.ActivateAccountRequest;
import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.dto.UpdateEmployeeRequest;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.exception.EmployeeException;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.service.impl.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
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

    @Mock
    private IEmailService emailService;

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
        when(passwordEncoder.encode(anyString()))
                .thenReturn("encodedPassword");

        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = employeeService.createEmployee(request);

        assertNotNull(response);

        verify(passwordEncoder, times(1)).encode(anyString());
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

        employeeService.activateEmployee(token);

        assertEquals("ACTIVE", employee.getStatus());
        assertNull(employee.getActivationToken());

        verify(employeeRepository, times(1)).save(employee);

    }

    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {
        String token = "invalid-token";

        ActivateAccountRequest request = new ActivateAccountRequest("newPassword");

        when(employeeRepository.findByActivationToken(token))
                .thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> {
            employeeService.activateEmployee(token);
        });

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        var response = employeeService.getEmployeeById(1L);

        assertNotNull(response);
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll())
                .thenReturn(List.of(new Employee(), new Employee()));

        var result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        verify(employeeRepository).findAll();
    }

    @Test
    void testUpdateEmployee() {
        Employee employee = new Employee();
        UpdateEmployeeRequest request =
                new UpdateEmployeeRequest(
                        "John",
                        "Doe",
                        LocalDate.of(1990, 1, 1),
                        "Male",
                        "American",
                        "johndoepersonal@email.com",
                        "New York",
                        "+1 212-345-6789",
                        "350 5th Avenue, New York, NY 10118",
                        "New York",
                        "USA",
                        "10118",
                        "NY",
                        "New York"
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        var response = employeeService.updateEmployee(1L, request);

        assertNotNull(response);
        verify(employeeRepository).save(employee);
    }

    @Test
    void testDeleteEmployee() {
        Employee employee = new Employee();

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }
}
