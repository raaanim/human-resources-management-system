package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.projectDto.*;
import aitho.ranim.hrms.enums.ProjectStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface IProjectController {
    ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest projectRequest);

    ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id);

    ResponseEntity<List<ProjectSummaryResponse>> getAllProjects(
            @RequestParam(required = false) ProjectStatus status,
            Pageable pageable);

    ResponseEntity<UpdateProjectResponse> updateProjectById(@Valid @RequestBody UpdateProjectRequest projectRequest, @PathVariable Long id);

    void deleteProject(@PathVariable Long id);
}
