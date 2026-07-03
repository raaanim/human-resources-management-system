package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.projectDto.*;
import aitho.ranim.hrms.entity.Project;
import aitho.ranim.hrms.enums.ProjectStatus;
import aitho.ranim.hrms.exception.ProjectException;
import aitho.ranim.hrms.repository.IProjectRepository;
import aitho.ranim.hrms.service.IProjectService;
import aitho.ranim.hrms.utils.ProjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService implements IProjectService {

    private final IProjectRepository projectRepository;

    public ProjectResponse createProject(ProjectRequest projectRequest) {
        Project project = ProjectUtils.createProject(projectRequest);
        projectRepository.save(project);
        return ProjectUtils.toProjectDetailResponse(project);
    }

    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> new ProjectException("Project not found", HttpStatus.NOT_FOUND, "/project/" + id));
        return ProjectUtils.toProjectDetailResponse(project);
}

    public Page<ProjectSummaryResponse> getAllProjects(
            ProjectStatus status,
            Pageable pageable) {

        Page<Project> projects;

        if (status != null) {
            projects = projectRepository.findByStatus(status, pageable);
        } else {
            projects = projectRepository.findAll(pageable);
        }

        return projects.map(ProjectUtils::toProjectSummaryResponse);
    }

    public UpdateProjectResponse updateProject(Long id, UpdateProjectRequest projectRequest) {
        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> new ProjectException("Project not found", HttpStatus.NOT_FOUND, "/project/" + id));
        ProjectUtils.updateProjectFromRequest(project, projectRequest);
        projectRepository.save(project);
        return new UpdateProjectResponse(
                LocalDate.now(),
                "Project successfully updated"
        );
     }

     public void deleteProject(Long id) {
         projectRepository
                 .findById(id)
                 .orElseThrow(() -> new ProjectException("Project not found", HttpStatus.NOT_FOUND, "/project/" + id));
         projectRepository.deleteById(id);
     }
}
