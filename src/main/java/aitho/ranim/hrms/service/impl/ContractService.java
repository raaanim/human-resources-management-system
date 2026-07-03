package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.contractDto.ContractRequest;
import aitho.ranim.hrms.dto.contractDto.ContractResponse;
import aitho.ranim.hrms.dto.contractDto.ContractSummaryResponse;
import aitho.ranim.hrms.entity.Contract;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.exception.AccessDeniedException;
import aitho.ranim.hrms.exception.ContractException;
import aitho.ranim.hrms.exception.EmployeeException;
import aitho.ranim.hrms.repository.IContractRepository;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.service.IContractService;
import aitho.ranim.hrms.utils.ContractUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ContractService implements IContractService {
    private final IContractRepository contractRepository;
    private final IEmployeeRepository employeeRepository;

   public ContractResponse createContract(ContractRequest contractRequest) {
       Long employeeId = contractRequest.employee().getId();
       Employee employee = employeeRepository.findById(employeeId)
               .orElseThrow(()-> new EmployeeException("Employee not found", HttpStatus.NOT_FOUND, "/employees/" + employeeId));
        Contract contract = ContractUtils.createContract(contractRequest);
        contract.setEmployee(employee);
       Contract savedContract = contractRepository.save(contract);
        return ContractUtils.getContractDetails(savedContract);
    }

    @Transactional(readOnly = true)
    public List<ContractSummaryResponse> getEmployeeContractDetails(Long employeeId) {
        checkAccess(employeeId);

        return contractRepository.findByEmployeeIdOrderByStartDateDesc(employeeId)
                .stream()
                .map(ContractUtils::getContractSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContractResponse getActiveContract(Long employeeId) {
       checkAccess(employeeId);
        Contract activeContract =
                contractRepository
                        .findFirstByEmployeeIdAndActiveTrue(employeeId)
                        .orElseThrow(()-> new ContractException("No active contract found", HttpStatus.NOT_FOUND, "/employee/" + employeeId + "/active"));
        return ContractUtils.getContractDetails(activeContract);
    }

    public ContractResponse updateContract(ContractRequest contractRequest, Long id) {
        Contract contract = contractRepository
                .findById(id)
                .orElseThrow(() -> new ContractException("Contract not found", HttpStatus.NOT_FOUND, "/contract/" + id));
        ContractUtils.updateContractFromRequest(contract, contractRequest);
        contractRepository.save(contract);
        return ContractUtils.getContractDetails(contract);
    }

    @Transactional
    public void activateContract(Long id) {

            Contract contract = contractRepository.findById(id)
                    .orElseThrow(() -> new ContractException("Contract not found", HttpStatus.NOT_FOUND, "/contract/" + id));

            Long employeeId = contract.getEmployee().getId();

            contractRepository.deactivateByEmployeeId(employeeId);
            contract.setActive(true);
            contractRepository.save(contract);
   }

    public void checkAccess(Long employeeId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getAuthorities() == null) {
            throw new AccessDeniedException("Access denied", HttpStatus.FORBIDDEN);
        }

        boolean isAdminOrHr = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                        || a.getAuthority().equals("ROLE_HR"));

        boolean isSelf = auth.getName().equals(employeeId.toString());

        if (!isAdminOrHr && !isSelf) {
            throw new AccessDeniedException("Access to this resource is not allowed", HttpStatus.FORBIDDEN);
        }
    }
}