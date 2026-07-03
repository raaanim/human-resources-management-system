package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.projectDto.ProjectHoursReportResponse;
import aitho.ranim.hrms.dto.timeEntryDto.TimeEntryRequest;
import aitho.ranim.hrms.dto.timeEntryDto.TimeEntryResponse;

import java.util.List;

public interface ITimeEntryService {
    TimeEntryResponse addHours(TimeEntryRequest timeEntryRequest);
    List<TimeEntryResponse> getMyEntries(String month);
    ProjectHoursReportResponse getProjectReport(Long projectId);
    void deleteEntry(Long id);
}
