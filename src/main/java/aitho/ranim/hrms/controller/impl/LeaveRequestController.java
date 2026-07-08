package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.ILeaveRequestController;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestResponse;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestReviewRequest;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestSubmitRequest;
import aitho.ranim.hrms.service.ILeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/leave-request")
public class LeaveRequestController implements ILeaveRequestController {
    private final ILeaveRequestService leaveRequestService;

    public LeaveRequestController(ILeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<LeaveRequestResponse> submitLeaveRequest(@Valid @RequestBody LeaveRequestSubmitRequest request) {
        LeaveRequestResponse response = leaveRequestService.submitRequest(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/review/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<LeaveRequestResponse> reviewLeaveRequest(@PathVariable Long id, @RequestBody LeaveRequestReviewRequest request) {
        LeaveRequestResponse response = leaveRequestService.reviewRequest(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelLeaveRequest(@PathVariable Long id) {
        leaveRequestService.cancelRequest(id);
    }

    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<LeaveRequestResponse>> getMyLeaveRequests() {
        List<LeaveRequestResponse> responses = leaveRequestService.getAllMyLeaveRequests();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<List<LeaveRequestResponse>> getAllPendingLeaveRequests() {
        List<LeaveRequestResponse> responses = leaveRequestService.getAllPendingLeaveRequestsForAdmin();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<List<LeaveRequestResponse>> getAllLeaveRequestsByEmployee(
            @PathVariable Long employeeId) {
        List<LeaveRequestResponse> responses =
                leaveRequestService.getAllLeaveRequestsMadeByEmployee(employeeId);
        return ResponseEntity.ok(responses);
    }
}
