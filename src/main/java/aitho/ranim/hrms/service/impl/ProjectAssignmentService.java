package aitho.ranim.hrms.service.impl;
import aitho.ranim.hrms.dto.AssignEmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeProjectAssignmentResponse;
import aitho.ranim.hrms.dto.ProjectAssignmentResponse;
import aitho.ranim.hrms.dto.ProjectEmployeeAssignmentResponse;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.Project;
import aitho.ranim.hrms.entity.ProjectAssignment;
import aitho.ranim.hrms.exception.ProjectAssignmentException;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.IProjectAssignmentRepository;
import aitho.ranim.hrms.repository.IProjectRepository;
import aitho.ranim.hrms.service.IProjectAssignmentService;
import aitho.ranim.hrms.utils.ProjectAssignmentUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectAssignmentService implements IProjectAssignmentService {

    private final IProjectAssignmentRepository projectAssignmentRepository;
    private final IEmployeeRepository employeeRepository;
    private final IProjectRepository projectRepository;

    public ProjectAssignmentResponse assignEmployeeToProject(AssignEmployeeRequest request) {
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new ProjectAssignmentException("Employee not found with ID " + request.employeeId(), HttpStatus.NOT_FOUND, "/assign"));
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ProjectAssignmentException("Project not found with ID " + request.projectId(), HttpStatus.NOT_FOUND, "/assign"));

        Optional<ProjectAssignment> projectAssignment =
                projectAssignmentRepository
                        .findByEmployeeIdAndProjectIdAndEndDateIsNull(request.employeeId(), request.projectId());

        if (projectAssignment.isPresent()) {
            throw new ProjectAssignmentException(
                    "Employee is already assigned to this project",
                    HttpStatus.CONFLICT,
                    "/assign"
            );
        }
        ProjectAssignment assignment = new ProjectAssignment();
        assignment.setEmployee(employee);
        assignment.setProject(project);
        assignment.setRole(request.role());
        assignment.setStartDate(request.startDate());
        assignment.setAssignedAt(LocalDate.now());

        ProjectAssignment savedAssignment = projectAssignmentRepository.save(assignment);

        return new ProjectAssignmentResponse(
                savedAssignment.getId(),
                employee.getId(),
                employee.getFirstName() + " " + employee.getLastName(),
                project.getId(),
                project.getName(),
                savedAssignment.getRole(),
                savedAssignment.getStartDate(),
                savedAssignment.getEndDate()
        );
    }

    public ProjectAssignmentResponse closeProjectAssignment(Long assignmentId) {
        ProjectAssignment projectAssignment = projectAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ProjectAssignmentException("Project assignment not found with ID " + assignmentId, HttpStatus.NOT_FOUND, "/close/" + assignmentId));
        if (projectAssignment.getEndDate() != null) {
            throw new ProjectAssignmentException(
                    "Project assignment is already closed",
                    HttpStatus.CONFLICT,
                    "/close/" + assignmentId
            );
        }
        projectAssignment.setEndDate(LocalDate.now());

        ProjectAssignment savedAssignment = projectAssignmentRepository.save(projectAssignment);
        return new ProjectAssignmentResponse(
                savedAssignment.getId(),
                savedAssignment.getEmployee().getId(),
                savedAssignment.getEmployee().getFirstName() + " " + savedAssignment.getEmployee().getLastName(),
                savedAssignment.getProject().getId(),
                savedAssignment.getProject().getName(),
                savedAssignment.getRole(),
                savedAssignment.getStartDate(),
                savedAssignment.getEndDate()
        );
    }
    public List<EmployeeProjectAssignmentResponse> getAssignmentsByProject(Long projectId) {
        return projectAssignmentRepository.findByProjectId(projectId)
                .stream()
                .map(ProjectAssignmentUtils::toEmployeeResponse)
                .collect(Collectors.toList());
    }


    public List<ProjectEmployeeAssignmentResponse> getAssignmentsByEmployee(Long employeeId) {
        return projectAssignmentRepository.findByEmployeeId(employeeId)
                .stream()
                .map(ProjectAssignmentUtils::toProjectResponse)
                .collect(Collectors.toList());
    }
}

