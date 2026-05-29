package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.ProjectRequest;
import aitho.ranim.hrms.dto.ProjectResponse;
import jakarta.validation.Valid;

public interface IProjectService {
    ProjectResponse createProject(@Valid ProjectRequest projectRequest);
}
