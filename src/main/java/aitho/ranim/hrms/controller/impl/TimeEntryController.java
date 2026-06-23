package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.ITimeEntryController;
import aitho.ranim.hrms.dto.ProjectHoursReportResponse;
import aitho.ranim.hrms.dto.TimeEntryRequest;
import aitho.ranim.hrms.dto.TimeEntryResponse;
import aitho.ranim.hrms.service.impl.TimeEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/time-entry")
public class TimeEntryController implements ITimeEntryController {

    private final TimeEntryService timeEntryService;

    public TimeEntryController(TimeEntryService timeEntryService) {
        this.timeEntryService = timeEntryService;
    }

    // POST http://localhost:8080/api/v1/time-entry
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @PostMapping
    public ResponseEntity<TimeEntryResponse> insertHours(@RequestBody TimeEntryRequest timeEntryRequest) {
        TimeEntryResponse response = timeEntryService.addHours(timeEntryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET http://localhost:8080/api/v1/time-entry/my_entries
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/my_entries")
        public ResponseEntity<List<TimeEntryResponse>> getMyEntries(@RequestParam String month) {
        List<TimeEntryResponse> responses = timeEntryService.getMyEntries(month);
        return ResponseEntity.ok(responses);
    }

    // GET http://localhost:8080/api/v1/time-entry/project/2/report
    @GetMapping("/project/{projectId}/report")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ProjectHoursReportResponse getProjectReport(@PathVariable Long projectId) {
        return timeEntryService.getProjectReport(projectId);
    }

    // DELETE http://localhost:8080/api/v1/time-entry/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public void deleteEntry(@PathVariable Long id) {
        timeEntryService.deleteEntry(id);
    }
}

