package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.IProjectController;
import aitho.ranim.hrms.dto.ProjectRequest;
import aitho.ranim.hrms.dto.ProjectResponse;
import aitho.ranim.hrms.service.IProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController implements IProjectController {

private final IProjectService projectService;

    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    // POST path = api/v1/project
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest projectRequest){
        ProjectResponse response = projectService.createProject(projectRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

