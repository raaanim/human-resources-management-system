package aitho.ranim.hrms.service;


import aitho.ranim.hrms.dto.*;
import aitho.ranim.hrms.entity.Project;
import aitho.ranim.hrms.enums.ProjectStatus;
import aitho.ranim.hrms.exception.ProjectException;
import aitho.ranim.hrms.repository.IProjectRepository;
import aitho.ranim.hrms.service.impl.ProjectService;
import aitho.ranim.hrms.utils.ProjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private IProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    public void givenValidProjectRequest_whenCreateProject_thenReturnCreatedProject() {
       ProjectRequest project = new ProjectRequest(
               "Project Vega",
               "Development of a real-time chat system for customer support",
               "Connectly",
               LocalDate.now(),
               ProjectStatus.PLANNED,
               LocalDate.now(),
               new BigDecimal("95000.00")
       );

        when(projectRepository.save(any(Project.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

       ProjectResponse result = projectService.createProject(project);
        assertNotNull(result);

        assertEquals("Project Vega", result.name());
        assertEquals("Connectly", result.clientName());
        assertEquals("Development of a real-time chat system for customer support", result.description());
        assertEquals(ProjectStatus.PLANNED, result.status());
        assertEquals(new BigDecimal("95000.00"), result.budget());

        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    public void givenExistingProjectId_whenGetProjectById_thenReturnProject() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Project Vega");
        project.setDescription("Development of a real-time chat system for customer support");
        project.setClientName("Connectly");
        project.setStartDate(LocalDate.now());
        project.setStatus(ProjectStatus.PLANNED);
        project.setEndDate(LocalDate.now());
        project.setBudget(new BigDecimal("95000.00"));

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        ProjectResponse result = projectService.getProjectById(1L);
        assertNotNull(result);

        assertEquals("Project Vega", result.name());
        assertEquals("Connectly", result.clientName());
        assertEquals("Development of a real-time chat system for customer support", result.description());
        assertEquals(ProjectStatus.PLANNED, result.status());
        assertEquals(new BigDecimal("95000.00"), result.budget());

        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    public void givenNonExistingProjectId_whenGetProjectById_thenThrowException() {
        when(projectRepository.findById(1L))
                .thenReturn(Optional.empty());

        ProjectException exception = assertThrows(
                ProjectException.class,
                () -> projectService.getProjectById(1L)
        );

        assertEquals("Project not found", exception.getMessage());

        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    public void givenValidProjectRequest_whenUpdateProject_thenReturnUpdatedProject() {
        Long id = 1L;

        Project existingProject = new Project();
        existingProject.setId(id);
        existingProject.setName("Old Name");

        UpdateProjectRequest project = new UpdateProjectRequest(
                "Project Vega",
                "Development of a real-time chat system for customer support",
                "Connectly",
                LocalDate.now(),
                ProjectStatus.IN_PROGRESS,
                LocalDate.now(),
                new BigDecimal("95000.00")
        );
        when(projectRepository.save(any(Project.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(existingProject));

        UpdateProjectResponse result = projectService.updateProject(id, project);
        LocalDate today = LocalDate.now();

        assertNotNull(result);
        assertEquals("Project successfully updated", result.message());
        assertEquals(today, result.dateOfUpdate());

        verify(projectRepository, times(1)).findById(id);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    public void givenInvalidId_whenUpdateProject_thenThrowException() {

        Long id = 1L;

        UpdateProjectRequest request = new UpdateProjectRequest(
                "New Name",
                "Updated description",
                "New Client",
                LocalDate.now(),
                ProjectStatus.IN_PROGRESS,
                LocalDate.now().plusMonths(1),
                new BigDecimal("120000.00")
        );

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());

        ProjectException exception = assertThrows(
                ProjectException.class,
                () -> projectService.updateProject(id, request)
        );

        assertEquals("Project not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("/project/1", exception.getPath());

        verify(projectRepository, times(1)).findById(id);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    public void givenStatus_whenGetAllProjects_thenReturnFilteredProjects() {

        Project project = new Project();
        project.setId(1L);
        project.setName("Project Vega");
        project.setClientName("Connectly");
        project.setStatus(ProjectStatus.IN_PROGRESS);

        List<Project> projectList = List.of(project);
        Page<Project> page = new PageImpl<>(projectList);

        Pageable pageable = PageRequest.of(0, 10);

        when(projectRepository.findByStatus(ProjectStatus.IN_PROGRESS, pageable))
                .thenReturn(page);

        Page<ProjectSummaryResponse> result =
                projectService.getAllProjects(ProjectStatus.IN_PROGRESS, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(ProjectStatus.IN_PROGRESS, result.getContent().get(0).status());

        verify(projectRepository, times(1))
                .findByStatus(ProjectStatus.IN_PROGRESS, pageable);

        verify(projectRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    public void givenExistingProjectId_whenDeleteProject_thenDeleteSuccessfully() {

        Long id = 1L;

        Project project = new Project();
        project.setId(id);

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        doNothing().when(projectRepository).deleteById(id);

        projectService.deleteProject(id);

        verify(projectRepository, times(1)).findById(id);
        verify(projectRepository, times(1)).deleteById(id);
    }

    @Test
    public void givenInvalidId_whenDeleteProject_thenThrowException() {

        Long id = 1L;

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());

        ProjectException exception = assertThrows(
                ProjectException.class,
                () -> projectService.deleteProject(id)
        );

        assertEquals("Project not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("/project/1", exception.getPath());

        verify(projectRepository, times(1)).findById(id);
        verify(projectRepository, never()).deleteById(anyLong());
    }
}
