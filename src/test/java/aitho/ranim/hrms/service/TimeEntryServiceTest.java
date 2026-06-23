package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.ProjectHoursReportResponse;
import aitho.ranim.hrms.dto.TimeEntryRequest;
import aitho.ranim.hrms.dto.TimeEntryResponse;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.Project;
import aitho.ranim.hrms.entity.Role;
import aitho.ranim.hrms.entity.TimeEntry;
import aitho.ranim.hrms.enums.RoleName;
import aitho.ranim.hrms.exception.ProjectAssignmentException;
import aitho.ranim.hrms.exception.ProjectException;
import aitho.ranim.hrms.exception.TimeEntryException;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.IProjectAssignmentRepository;
import aitho.ranim.hrms.repository.IProjectRepository;
import aitho.ranim.hrms.repository.ITimeEntryRepository;
import aitho.ranim.hrms.service.impl.TimeEntryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static aitho.ranim.hrms.enums.RoleName.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimeEntryServiceTest {

    @Mock
    IProjectAssignmentRepository projectAssignmentRepository;
    @Mock
    IEmployeeRepository employeeRepository;
    @Mock
    IProjectRepository projectRepository;
    @Mock
    ITimeEntryRepository timeEntryRepository;
    @InjectMocks
    TimeEntryService timeEntryService;

    @Test
    public void givenTimeEntryRequest_whenValid_thenReturnsTimeEntry() {
        {
            String email = "mario.rossi@test.com";

            Authentication authentication = mock(Authentication.class);
            when(authentication.getName()).thenReturn(email);

            SecurityContext context = mock(SecurityContext.class);
            when(context.getAuthentication()).thenReturn(authentication);

            SecurityContextHolder.setContext(context);

            Employee employee = new Employee();
            employee.setId(1L);
            employee.setEmail(email);

            Project project = new Project();
            project.setId(10L);

            TimeEntryRequest timeEntryRequest = new TimeEntryRequest(
                    10L,
                    LocalDate.of(2025, 1, 20),
                    BigDecimal.valueOf(8),
                    "Backend development"
            );

            when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));
            when(projectRepository.findById(10L)).thenReturn(Optional.of(project));
            when(projectAssignmentRepository.existsByEmployeeIdAndProjectIdAndEndDateIsNull(1L, 10L)).thenReturn(true);
            when(timeEntryRepository.existsByEmployeeIdAndProjectIdAndDate(1L, 10L, LocalDate.of(2025, 1, 20))).thenReturn(false);

            TimeEntryResponse response = timeEntryService.addHours(timeEntryRequest);
            verify(timeEntryRepository).save(any(TimeEntry.class));
            assertNotNull(response);
        }
    }

    @Test
    void shouldThrowExceptionWhenTimeEntryAlreadyExists() {
        setupSecurity("test@test.com");

        Employee employee = new Employee();
        employee.setId(1L);

        Project project = new Project();
        project.setId(2L);

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(employee));

        when(projectRepository.findById(2L))
                .thenReturn(Optional.of(project));

        when(projectAssignmentRepository
                .existsByEmployeeIdAndProjectIdAndEndDateIsNull(1L, 2L))
                .thenReturn(true);

        when(timeEntryRepository
                .existsByEmployeeIdAndProjectIdAndDate(anyLong(), anyLong(), any()))
                .thenReturn(true);

        TimeEntryRequest request =
                new TimeEntryRequest(
                        2L,
                        LocalDate.now(),
                        BigDecimal.valueOf(8),
                        "test"
                );

        assertThrows(
                ProjectAssignmentException.class,
                () -> timeEntryService.addHours(request)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {

        String email = "test@test.com";

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        when(employeeRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> timeEntryService.addHours(mock(TimeEntryRequest.class))
        );
    }

    @Test
    void shouldThrowExceptionWhenProjectNotFound() {

        String email = "test@test.com";

        setupSecurity(email);

        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeRepository.findByEmail(email))
                .thenReturn(Optional.of(employee));

        when(projectRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        TimeEntryRequest request =
                new TimeEntryRequest(
                        1L,
                        LocalDate.now(),
                        BigDecimal.valueOf(8),
                        "test"
                );

        assertThrows(
                ProjectAssignmentException.class,
                () -> timeEntryService.addHours(request)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotAssigned() {

        setupSecurity("test@test.com");

        Employee employee = new Employee();
        employee.setId(1L);

        Project project = new Project();
        project.setId(2L);

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(employee));

        when(projectRepository.findById(anyLong()))
                .thenReturn(Optional.of(project));

        when(projectAssignmentRepository
                .existsByEmployeeIdAndProjectIdAndEndDateIsNull(1L, 2L))
                .thenReturn(false);

        TimeEntryRequest request =
                new TimeEntryRequest(
                        2L,
                        LocalDate.now(),
                        BigDecimal.valueOf(8),
                        "test"
                );

        assertThrows(
                ProjectAssignmentException.class,
                () -> timeEntryService.addHours(request)
        );
    }

    @Test
    void givenValidEmployeeAndMonth_whenGetMyEntries_thenReturnEntries() {
        setupSecurity("mario@test.com");

        Employee employee = new Employee();
        employee.setId(1L);

        Project project = new Project();
        project.setId(1L);
        project.setName("CRM");

        TimeEntry entry = new TimeEntry();
        entry.setEmployee(employee);
        entry.setProject(project);
        entry.setId(1L);
        entry.setDate(LocalDate.of(2025, 1, 15));
        entry.setDescription("Worked on project X");
        entry.setHoursWorked(BigDecimal.valueOf(8));
        entry.setCreatedAt(LocalDate.of(2025, 1, 15));


        when(employeeRepository.findByEmail("mario@test.com"))
                .thenReturn(Optional.of(employee));

        when(timeEntryRepository.findByEmployeeAndDateBetween(
                eq(employee),
                any(LocalDate.class),
                any(LocalDate.class)
        )).thenReturn(List.of(entry));

        List<TimeEntryResponse> result =
                timeEntryService.getMyEntries("2025-01");

        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowWhenEmployeeNotFoundInGetMyEntries() {

        setupSecurity("test@test.com");

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> timeEntryService.getMyEntries("2025-01")
        );
    }

    @Test
    void shouldThrowWhenMonthFormatIsInvalid() {

        setupSecurity("mario@test.com");

        Employee employee = new Employee();

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(employee));

        assertThrows(
                DateTimeParseException.class,
                () -> timeEntryService.getMyEntries("pippo")
        );
    }


    @Test
    void givenExistingProject_whenGetProjectReport_thenReturnProjectReport() {

        Project project = new Project();
        project.setId(1L);
        project.setName("CRM");

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Mario");

        TimeEntry entry = new TimeEntry();
        entry.setId(1L);
        entry.setProject(project);
        entry.setEmployee(employee);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(timeEntryRepository.findByProjectId(1L))
                .thenReturn(List.of(entry));

        when(timeEntryRepository.getTotalHoursByProject(1L))
                .thenReturn(BigDecimal.valueOf(40));

        ProjectHoursReportResponse response =
                timeEntryService.getProjectReport(1L);

        assertEquals(1L, response.projectId());
        assertEquals("CRM", response.projectName());
        assertEquals(BigDecimal.valueOf(40), response.totalHours());
    }

    @Test
    void shouldThrowWhenProjectNotFoundInReport() {

        when(projectRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProjectException.class,
                () -> timeEntryService.getProjectReport(1L)
        );
    }

    @Test
    void givenAdminUserAndExistingTimeEntry_whenDeleteEntry_thenDeleteSuccessfully(){
        setupSecurity("user@test.com");

        Employee employee = new Employee();
        employee.setId(1L);

        TimeEntry entry = new TimeEntry();
        entry.setEmployee(employee);

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(employee));

        when(timeEntryRepository.findById(10L))
                .thenReturn(Optional.of(entry));

        timeEntryService.deleteEntry(10L);

        verify(timeEntryRepository).delete(entry);
    }

    @Test
    void shouldThrowWhenEmployeeNotFound() {

        setupSecurity("user@test.com");

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> timeEntryService.deleteEntry(10L)
        );
    }

    @Test
    void shouldThrowWhenTimeEntryNotFound() {

        setupSecurity("user@test.com");

        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(employee));

        when(timeEntryRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                TimeEntryException.class,
                () -> timeEntryService.deleteEntry(10L)
        );
    }

    @Test
    void shouldDeleteWhenUserIsAdmin() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "admin@test.com",
                        null,
                        List.of(
                                new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name())
                        )
                );

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        Employee admin = new Employee();
        admin.setId(100L);

        Employee owner = new Employee();
        owner.setId(1L);

        TimeEntry entry = new TimeEntry();
        entry.setEmployee(owner);

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(admin));

        when(timeEntryRepository.findById(10L))
                .thenReturn(Optional.of(entry));

        timeEntryService.deleteEntry(10L);

        verify(timeEntryRepository).delete(entry);
    }

    @Test
    void shouldThrowWhenUserIsNotOwnerAndNotAdmin() {

        setupSecurity("user@test.com");

        Employee loggedUser = new Employee();
        loggedUser.setId(1L);

        Employee owner = new Employee();
        owner.setId(2L);

        TimeEntry entry = new TimeEntry();
        entry.setEmployee(owner);

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(loggedUser));

        when(timeEntryRepository.findById(10L))
                .thenReturn(Optional.of(entry));

        assertThrows(
                TimeEntryException.class,
                () -> timeEntryService.deleteEntry(10L)
        );

        verify(timeEntryRepository, never())
                .delete(any());
    }

    private void setupSecurity(String email) {

        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(email);

        SecurityContext context = mock(SecurityContext.class);

        when(context.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(context);
    }


}
