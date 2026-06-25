package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.projectDto.*;
import aitho.ranim.hrms.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(
        name = "Project Controller",
        description = "Endpoints for managing projects"
)
public interface IProjectController {
    @Operation(
            summary = "Create Project",
            description = "Creates a new project. Accessible by ADMIN and HR roles."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest projectRequest);
    @Operation(
            summary = "Get Project by ID",
            description = "Retrieves project details by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id);
    @Operation(
            summary = "Get All Projects",
            description = "Retrieves all projects with optional filtering by status and pagination."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid query parameters"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<List<ProjectSummaryResponse>> getAllProjects(
            @RequestParam(required = false) ProjectStatus status,
            Pageable pageable);
    @Operation(
            summary = "Update Project",
            description = "Updates project details by ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<UpdateProjectResponse> updateProjectById(@Valid @RequestBody UpdateProjectRequest projectRequest, @PathVariable Long id);
    @Operation(
            summary = "Delete Project",
            description = "Deletes a project by ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    void deleteProject(@PathVariable Long id);
}
