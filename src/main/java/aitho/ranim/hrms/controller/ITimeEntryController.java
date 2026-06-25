package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.projectDto.ProjectHoursReportResponse;
import aitho.ranim.hrms.dto.timeEntryDto.TimeEntryRequest;
import aitho.ranim.hrms.dto.timeEntryDto.TimeEntryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Time Entry Controller", description = "Endpoints for managing time entries")
public interface ITimeEntryController {
    @Operation(
            summary = "Insert Hours",
            description = "Creates a new time entry for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Time entry created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    ResponseEntity<TimeEntryResponse> insertHours(@RequestBody TimeEntryRequest timeEntryRequest);
    @Operation(
            summary = "Get My Time Entries",
            description = "Retrieves time entries for the authenticated user filtered by month."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Time entries retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid month format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    ResponseEntity<List<TimeEntryResponse>> getMyEntries(@RequestParam String month);
    @Operation(
            summary = "Get Project Report",
            description = "Returns total worked hours for a specific project."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project report retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    ProjectHoursReportResponse getProjectReport(@PathVariable Long projectId);
    @Operation(
            summary = "Delete Time Entry",
            description = "Deletes a time entry by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Time entry deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Time entry not found")
    })
    void deleteEntry(@PathVariable Long id);
}
