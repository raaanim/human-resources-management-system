package aitho.ranim.hrms.service.impl;
import aitho.ranim.hrms.dto.ProjectHoursReportResponse;
import aitho.ranim.hrms.dto.TimeEntryRequest;
import aitho.ranim.hrms.dto.TimeEntryResponse;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.Project;
import aitho.ranim.hrms.entity.TimeEntry;
import aitho.ranim.hrms.exception.ProjectAssignmentException;
import aitho.ranim.hrms.exception.ProjectException;
import aitho.ranim.hrms.exception.TimeEntryException;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.IProjectAssignmentRepository;
import aitho.ranim.hrms.repository.IProjectRepository;
import aitho.ranim.hrms.repository.ITimeEntryRepository;
import aitho.ranim.hrms.service.ITimeEntryService;
import aitho.ranim.hrms.utils.TimeEntryUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeEntryService implements ITimeEntryService {

    private final IProjectAssignmentRepository projectAssignmentRepository;
    private final IEmployeeRepository employeeRepository;
    private final IProjectRepository projectRepository;
    private final ITimeEntryRepository timeEntryRepository;

    public TimeEntryResponse addHours(TimeEntryRequest timeEntryRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        Project project = projectRepository.findById(timeEntryRequest.projectId())
                .orElseThrow(() -> new ProjectAssignmentException("Project not found", HttpStatus.NOT_FOUND, "/project"));

        boolean assigned = projectAssignmentRepository.existsByEmployeeIdAndProjectIdAndEndDateIsNull(employee.getId(), project.getId());

        if (!assigned) {
            throw new ProjectAssignmentException(
                    "The employee is not assigned to this project",
                    HttpStatus.FORBIDDEN,
                    "/time-entry"
            );
        }

        boolean alreadyExists = timeEntryRepository.existsByEmployeeIdAndProjectIdAndDate(
                        employee.getId(),
                        project.getId(),
                        timeEntryRequest.date()
                );

        if (alreadyExists) {
            throw new ProjectAssignmentException(
                    "TimeEntry already exists for this day and project",
                    HttpStatus.CONFLICT,
                    "/time-entry"
            );
        }

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setEmployee(employee);
        timeEntry.setProject(project);
        timeEntry.setDate(timeEntryRequest.date());
        timeEntry.setHoursWorked(timeEntryRequest.hoursWorked());
        timeEntry.setDescription(timeEntryRequest.description());
        timeEntry.setCreatedAt(LocalDate.now());
        timeEntryRepository.save(timeEntry);

        return TimeEntryUtils.toTimeEntryDetailResponse(timeEntry);
    }

    public List<TimeEntryResponse> getMyEntries(String month) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        YearMonth yearMonth = YearMonth.parse(month);

        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        return timeEntryRepository
                .findByEmployeeAndDateBetween(employee, start, end)
                .stream()
                .map(TimeEntryUtils::toTimeEntryDetailResponse)
                .toList();

    }
    public ProjectHoursReportResponse getProjectReport(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(
                        "Project not found",
                        HttpStatus.NOT_FOUND,
                        "/project" + projectId + "/report"
                ));

        List<TimeEntryResponse> entries =
                timeEntryRepository
                        .findByProjectId(projectId)
                        .stream()
                        .map(TimeEntryUtils::toTimeEntryDetailResponse)
                        .toList();

        BigDecimal totalHours =
                timeEntryRepository.getTotalHoursByProject(projectId);

        return new ProjectHoursReportResponse(
                project.getId(),
                project.getName(),
                totalHours,
                entries
        );
    }

    public void deleteEntry(Long id) {

            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

            TimeEntry entry = timeEntryRepository.findById(id)
                    .orElseThrow(() -> new TimeEntryException(
                            "TimeEntry not found",
                            HttpStatus.NOT_FOUND,
                            "/time-entries/" + id
                    ));

            boolean isAdmin = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            boolean isOwner = entry.getEmployee().getId().equals(employee.getId());

            if (!isAdmin && !isOwner) {
                throw new TimeEntryException(
                        "You cannot delete this time entry",
                        HttpStatus.FORBIDDEN,
                        "/time-entries/" + id
               );
            }

            timeEntryRepository.delete(entry);
        }
    }