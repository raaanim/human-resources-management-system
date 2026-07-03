package aitho.ranim.hrms.dto.leaveBalanceDto;

import lombok.Builder;

public record LeaveAccrualProcessResponse(
        int processedEmployees,
        int skippedEmployees,
        int missingContractEmployees

) {
}
