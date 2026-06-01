package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.*;
import aitho.ranim.hrms.enums.ProjectStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IProjectService {
    ProjectResponse createProject(@Valid ProjectRequest projectRequest);

    ProjectResponse getProjectById(Long id);

    Page<ProjectSummaryResponse> getAllProjects(
            ProjectStatus status,
            Pageable pageable);

    UpdateProjectResponse updateProject(Long id, UpdateProjectRequest projectRequest);

    void deleteProject(Long id);
}
