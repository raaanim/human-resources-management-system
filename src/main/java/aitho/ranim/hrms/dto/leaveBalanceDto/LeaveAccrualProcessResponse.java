package aitho.ranim.hrms.dto.leaveBalanceDto;


public record LeaveAccrualProcessResponse(
        int processedEmployees,
        int skippedEmployees,
        int missingContractEmployees

) {
}
