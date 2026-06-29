package aitho.ranim.hrms.utils;

import aitho.ranim.hrms.dto.projectDto.ProjectRequest;
import aitho.ranim.hrms.dto.projectDto.ProjectResponse;
import aitho.ranim.hrms.dto.projectDto.ProjectSummaryResponse;
import aitho.ranim.hrms.dto.projectDto.UpdateProjectRequest;
import aitho.ranim.hrms.entity.Project;
import lombok.experimental.UtilityClass;
import java.time.LocalDate;

@UtilityClass
public class ProjectUtils {

    public Project createProject(ProjectRequest projectRequest) {
       Project project = new Project();
       project.setName(projectRequest.name());
       project.setClientName(projectRequest.clientName());
       project.setDescription(projectRequest.description());
       project.setStartDate(projectRequest.startDate());
       project.setStatus(projectRequest.status());
       project.setEndDate(projectRequest.endDate());
       project.setBudget(projectRequest.budget());
       project.setCreatedAt(LocalDate.now());
       return project;
    }

    public ProjectResponse toProjectDetailResponse (Project project) {
        return new ProjectResponse(
                project.getName(),
                project.getDescription(),
                project.getClientName(),
                project.getStartDate(),
                project.getStatus(),
                project.getEndDate(),
                project.getBudget()
        );
    }

    public ProjectSummaryResponse toProjectSummaryResponse (Project project) {
        return new ProjectSummaryResponse(
                project.getId(),
                project.getName(),
                project.getClientName(),
                project.getStatus()
        );
    }

      public void updateProjectFromRequest(Project project, UpdateProjectRequest newProject) {
        if (newProject.name() != null){
            project.setName(newProject.name());   }
          if (newProject.description() != null){
            project.setDescription(newProject.description());    }
          if (newProject.clientName() != null){
            project.setClientName(newProject.clientName());    }
          if (newProject.startDate() != null){
            project.setStartDate(newProject.startDate());    }
          if (newProject.endDate() != null){
              project.setEndDate(newProject.endDate());    }
          if (newProject.budget() != null){
            project.setBudget(newProject.budget());    }
    }
}
