package aitho.ranim.hrms.config;

import aitho.ranim.hrms.entity.Project;
import aitho.ranim.hrms.exception.ProjectException;
import aitho.ranim.hrms.repository.IProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("projectSecurity")
public class ProjectSecurity {

  private final IProjectRepository projectRepository;

    public ProjectSecurity(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public boolean isEmployeeAssignedToProject(Authentication authentication, Long id) {
            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new ProjectException("Project not found", HttpStatus.NOT_FOUND, "project/{id}"));

            return project.getId() == id;
        }
    }
