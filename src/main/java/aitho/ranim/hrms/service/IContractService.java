package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.contractDto.ContractRequest;
import aitho.ranim.hrms.dto.contractDto.ContractResponse;
import aitho.ranim.hrms.dto.contractDto.ContractSummaryResponse;

import java.util.List;

public interface IContractService {
    ContractResponse createContract(ContractRequest contractRequest);
    List<ContractSummaryResponse> getEmployeeContractDetails(Long employeeId);
    ContractResponse getActiveContract(Long employeeId);
    ContractResponse updateContract(ContractRequest contractRequest, Long id);
    void activateContract(Long id);
    void checkAccess(Long employeeId);
}
