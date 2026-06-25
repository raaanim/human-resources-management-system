package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.IProjectController;
import aitho.ranim.hrms.dto.projectDto.*;
import aitho.ranim.hrms.enums.ProjectStatus;
import aitho.ranim.hrms.service.IProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/project")
public class ProjectController implements IProjectController {

    private final IProjectService projectService;

    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    // POST path = http://localhost:8080/api/v1/project
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest projectRequest) {
        ProjectResponse response = projectService.createProject(projectRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //GET path = http://localhost:8080/api/v1/project/id
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        ProjectResponse response = projectService.getProjectById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //GET path = http://localhost:8080/api/v1/project
    // GET /api/v1/project?status=IN_PROGRESS&page=0&size=10
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getAllProjects(
            @RequestParam(required = false) ProjectStatus status,
            Pageable pageable) {

        Page<ProjectSummaryResponse> page =
                projectService.getAllProjects(status, pageable);

        return ResponseEntity.ok(page.getContent());
    }

    //PATCH path = http://localhost:8080/api/v1/project/update/id
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PatchMapping("update/{id}")
    public ResponseEntity<UpdateProjectResponse> updateProjectById(@Valid @RequestBody UpdateProjectRequest projectRequest, @PathVariable Long id) {
    UpdateProjectResponse response = projectService.updateProject(id, projectRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //DELETE path = http://localhost:8080/api/v1/project/delete/id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}

