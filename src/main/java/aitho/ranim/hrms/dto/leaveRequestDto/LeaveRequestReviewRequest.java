package aitho.ranim.hrms.dto.leaveRequestDto;

import aitho.ranim.hrms.enums.LeaveRequestsStatus;

public record LeaveRequestReviewRequest(
        LeaveRequestsStatus status,
        String reviewerNotes
) {
}
