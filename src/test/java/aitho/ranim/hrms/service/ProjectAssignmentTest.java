package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.employeeDto.AssignEmployeeRequest;
import aitho.ranim.hrms.dto.projectAssignmentDto.ProjectAssignmentResponse;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.Project;
import aitho.ranim.hrms.entity.ProjectAssignment;
import aitho.ranim.hrms.enums.AssignmentRole;
import aitho.ranim.hrms.exception.ProjectAssignmentException;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.IProjectAssignmentRepository;
import aitho.ranim.hrms.repository.IProjectRepository;
import aitho.ranim.hrms.service.impl.ProjectAssignmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectAssignmentTest {

    @Mock
    private IProjectAssignmentRepository repository;

    @Mock
    private IEmployeeRepository employeeRepository;

    @Mock
    private IProjectRepository projectRepository;

    @InjectMocks
    private ProjectAssignmentService projectService;

    @Test
    void shouldAssignEmployeeToProjectSuccessfully() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Mario");
        employee.setLastName("Rossi");

        Project project = new Project();
        project.setId(10L);
        project.setName("HRMS");

        AssignEmployeeRequest request =
                new AssignEmployeeRequest(1L, 10L, AssignmentRole.DEVELOPER, LocalDate.now());

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(10L)).thenReturn(Optional.of(project));
        when(repository.findByEmployeeIdAndProjectIdAndEndDateIsNull(1L, 10L))
                .thenReturn(Optional.empty());

        ProjectAssignment saved = new ProjectAssignment();
        saved.setId(100L);
        saved.setEmployee(employee);
        saved.setProject(project);
        saved.setRole(AssignmentRole.DEVELOPER);
        saved.setStartDate(LocalDate.now());

        when(repository.save(any(ProjectAssignment.class))).thenReturn(saved);

        ProjectAssignmentResponse response = projectService.assignEmployeeToProject(request);

        assertEquals(100L, response.id());
        assertEquals(1L, response.employeeId());
        assertEquals(10L, response.projectId());
    }

    @Test
    void shouldThrowConflictWhenAssignmentAlreadyExists() {

        Employee employee = new Employee();
        employee.setId(1L);

        Project project = new Project();
        project.setId(10L);

        AssignEmployeeRequest request =
                new AssignEmployeeRequest(1L, 10L, AssignmentRole.DEVELOPER, LocalDate.now());

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(10L)).thenReturn(Optional.of(project));

        ProjectAssignment existing = new ProjectAssignment();
        existing.setId(99L);

        when(repository.findByEmployeeIdAndProjectIdAndEndDateIsNull(1L, 10L))
                .thenReturn(Optional.of(existing));

        ProjectAssignmentException ex = assertThrows(
                ProjectAssignmentException.class,
                () -> projectService.assignEmployeeToProject(request)
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Employee is already assigned to this project", ex.getMessage());
    }
}
