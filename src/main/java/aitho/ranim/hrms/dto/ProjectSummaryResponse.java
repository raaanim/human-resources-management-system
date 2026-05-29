package aitho.ranim.hrms.dto;

import aitho.ranim.hrms.enums.ProjectStatus;

public record ProjectSummaryResponse(
        String id,
        String name,
        String clientName,
        ProjectStatus status
) {
}
