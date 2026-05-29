package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.ProjectRequest;
import aitho.ranim.hrms.dto.ProjectResponse;
import aitho.ranim.hrms.entity.Project;
import aitho.ranim.hrms.repository.IProjectRepository;
import aitho.ranim.hrms.service.IProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService implements IProjectService {

    private final IProjectRepository projectRepository;

    public ProjectResponse createProject(ProjectRequest request) {
        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setClientName(request.clientName());
        project.setStatus(request.status());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        project.setBudget(request.budget());

        projectRepository.save(project);

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

}
