package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.ITimeEntryController;
import aitho.ranim.hrms.dto.projectDto.ProjectHoursReportResponse;
import aitho.ranim.hrms.dto.timeEntryDto.TimeEntryRequest;
import aitho.ranim.hrms.dto.timeEntryDto.TimeEntryResponse;
import aitho.ranim.hrms.service.impl.TimeEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@Tag(name = "Time Entry Controller", description = "Endpoints for managing time entries")
@RequestMapping("/api/v1/time-entry")
public class TimeEntryController implements ITimeEntryController {

    private final TimeEntryService timeEntryService;

    public TimeEntryController(TimeEntryService timeEntryService) {
        this.timeEntryService = timeEntryService;
    }

    // POST http://localhost:8080/api/v1/time-entry
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @PostMapping
    @Operation(summary = "Insert Hours", description = "Inserts a new time entry for the logged-in user.")
    @ApiResponse(responseCode = "201", description = "Time entry created successfully")
    public ResponseEntity<TimeEntryResponse> insertHours(@RequestBody TimeEntryRequest timeEntryRequest) {
        TimeEntryResponse response = timeEntryService.addHours(timeEntryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET http://localhost:8080/api/v1/time-entry/my_entries
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/my_entries")
    @Operation(summary = "Get My Time Entries", description = "Retrieves time entries for the logged-in user.")
    @ApiResponse(responseCode = "200", description = "Time entries retrieved successfully")
    public ResponseEntity<List<TimeEntryResponse>> getMyEntries(@RequestParam String month) {
        List<TimeEntryResponse> responses = timeEntryService.getMyEntries(month);
        return ResponseEntity.ok(responses);
    }

    // GET http://localhost:8080/api/v1/time-entry/project/2/report
    @GetMapping("/project/{projectId}/report")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @Operation(summary = "Get Project Report", description = "Retrieves a report of hours logged for a specific project.")
    @ApiResponse(responseCode = "200", description = "Project report retrieved successfully")
    public ProjectHoursReportResponse getProjectReport(@PathVariable Long projectId) {
        return timeEntryService.getProjectReport(projectId);
    }

    // DELETE http://localhost:8080/api/v1/time-entry/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Time Entry", description = "Deletes a specific time entry by its ID.")
    @ApiResponse(responseCode = "204", description = "Time entry deleted successfully")
    public void deleteEntry(@PathVariable Long id) {
        timeEntryService.deleteEntry(id);
    }
}

