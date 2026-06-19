package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.TimeEntryRequest;
import aitho.ranim.hrms.dto.TimeEntryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ITimeEntryController {
    ResponseEntity<TimeEntryResponse> insertHours(@RequestBody TimeEntryRequest timeEntryRequest);

    ResponseEntity<List<TimeEntryResponse>> getMyEntries(@RequestParam String month);
}
