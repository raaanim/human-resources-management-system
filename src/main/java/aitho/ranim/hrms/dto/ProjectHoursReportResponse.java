package aitho.ranim.hrms.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProjectHoursReportResponse(
        Long projectId,
        String projectName,
        BigDecimal totalHours,
        List<TimeEntryResponse> entries
) {
}
