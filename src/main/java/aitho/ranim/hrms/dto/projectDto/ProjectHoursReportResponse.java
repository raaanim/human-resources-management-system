package aitho.ranim.hrms.dto.projectDto;

import aitho.ranim.hrms.dto.timeEntryDto.TimeEntryResponse;

import java.math.BigDecimal;
import java.util.List;

public record ProjectHoursReportResponse(
        Long projectId,
        String projectName,
        BigDecimal totalHours,
        List<TimeEntryResponse> entries
) {
}
