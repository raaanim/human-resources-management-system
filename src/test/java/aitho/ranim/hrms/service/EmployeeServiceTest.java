package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.*;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.Role;
import aitho.ranim.hrms.enums.RoleName;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.IRoleRepository;
import aitho.ranim.hrms.service.impl.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
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
    private IRoleRepository  roleRepository;

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
                "New York",
                RoleName.ROLE_EMPLOYEE
        );

        Role role = new Role();
        role.setName(request.role());

        when(roleRepository.findByName(request.role()))
                .thenReturn(Optional.of(role));

        when(passwordEncoder.encode(anyString()))
                .thenReturn("encodedPassword");

        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = employeeService.createEmployee(request);

        assertNotNull(response);

        verify(roleRepository, times(1)).findByName(request.role());
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

        Pageable pageable = PageRequest.of(0, 10);

        Employee e1 = new Employee();
        Employee e2 = new Employee();

        Page<Employee> page = new PageImpl<>(List.of(e1, e2));

        when(employeeRepository.findAll(pageable))
                .thenReturn(page);

        Page<EmployeeSummaryResponse> result =
                employeeService.getAllEmployees(pageable);

        assertEquals(2, result.getContent().size());

        verify(employeeRepository).findAll(pageable);
    }

    @Test
    void testUpdateEmployee_ShouldNotOverwriteNullFields() {
        Employee employee = getEmployee();

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));


        UpdateEmployeeRequest request = UpdateEmployeeRequest.builder()
                        .firstName("Johnny")
                        .lastName("Doe")
                        .build();

        UpdateEmployeeResponse response = employeeService.updateEmployee(1L, request);
        assertNotNull(response);
        assertEquals("Johnny", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("Male", employee.getGender());
        assertEquals("American", employee.getNationality());
        assertEquals(LocalDate.of(1990, 1, 1), employee.getDateOfBirth());
        assertEquals("johndoepersonal@email.com", employee.getPersonalEmail());
        assertEquals("johndoe@email.com", employee.getEmail());
        assertEquals("+1 212-345-6789", employee.getPhoneNumber());
        assertEquals("New York", employee.getBirthPlace());
        assertEquals("350 5th Avenue, New York, NY 10118", employee.getAddress());
        assertEquals("New York", employee.getCity());
        assertEquals("USA", employee.getCountry());
        assertEquals("10118", employee.getPostalCode());
        assertEquals("NY", employee.getProvince());
        assertEquals("New York", employee.getWorkLocation());
        verify(employeeRepository).save(employee);
    }

    public static Employee getEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setGender("Male");
        employee.setNationality("American");
        employee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employee.setPersonalEmail("johndoepersonal@email.com");
        employee.setEmail("johndoe@email.com");
        employee.setPhoneNumber("+1 212-345-6789");
        employee.setBirthPlace("New York");
        employee.setAddress("350 5th Avenue, New York, NY 10118");
        employee.setCity("New York");
        employee.setCountry("USA");
        employee.setPostalCode("10118");
        employee.setProvince("NY");
        employee.setWorkLocation("New York");
        return employee;
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
