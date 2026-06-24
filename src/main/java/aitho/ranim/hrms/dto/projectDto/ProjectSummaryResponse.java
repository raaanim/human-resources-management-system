package aitho.ranim.hrms.dto.projectDto;

import aitho.ranim.hrms.enums.ProjectStatus;

public record ProjectSummaryResponse(
        Long id,
        String name,
        String clientName,
        ProjectStatus status
) {
}
