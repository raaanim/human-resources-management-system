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
}
