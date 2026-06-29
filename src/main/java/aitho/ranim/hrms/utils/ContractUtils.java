package aitho.ranim.hrms.utils;

import aitho.ranim.hrms.dto.contractDto.ContractRequest;
import aitho.ranim.hrms.dto.contractDto.ContractResponse;
import aitho.ranim.hrms.dto.contractDto.ContractSummaryResponse;
import aitho.ranim.hrms.entity.Contract;
import lombok.experimental.UtilityClass;
import java.math.BigDecimal;
import java.time.LocalDate;


@UtilityClass
public class ContractUtils {
    public Contract createContract(ContractRequest contractRequest) {
        Contract contract = new Contract();
        contract.setStartDate(contractRequest.startDate());
        contract.setEndDate(contractRequest.endDate());
        contract.setContractType(contractRequest.contractType());
        contract.setPosition(contractRequest.position());
        contract.setDepartment(contractRequest.department());
        contract.setGrossAnnualSalary(contractRequest.grossAnnualSalary());
        contract.setMonthlyLeaveDays(contractRequest.monthlyLeaveDays());
        contract.setMonthlyLeaveHours(contractRequest.monthlyLeaveHours());
        contract.setNotes(contractRequest.notes());
        return contract;
    }

    public ContractResponse getContractDetails(Contract contract) {
        BigDecimal annualLeaveDays = contract.getMonthlyLeaveDays() == null
                ? null
                : contract.getMonthlyLeaveDays().multiply(BigDecimal.valueOf(12));

        return new ContractResponse(
                contract.getId(),
                contract.getEmployee(),
                contract.getContractType(),
                contract.getPosition(),
                contract.getDepartment(),
                contract.getGrossAnnualSalary(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.isActive(),
                contract.getMonthlyLeaveDays(),
                contract.getMonthlyLeaveHours(),
                contract.getNotes(),
                contract.getCreatedAt(),
                annualLeaveDays

        );
    }
    
    public ContractSummaryResponse getContractSummary(Contract contract) {
        return new ContractSummaryResponse(
                contract.getId(), 
                contract.getContractType(),
                contract.getPosition(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.isActive(),
                contract.getMonthlyLeaveDays(),
                contract.getMonthlyLeaveHours()
        );
    }

    public void updateContractFromRequest(Contract contract, ContractRequest newContractRequest) {
        if (newContractRequest.employee() != null){
            contract.setEmployee(newContractRequest.employee());
        }
        if (newContractRequest.contractType() != null){
            contract.setContractType(newContractRequest.contractType());
        }
        if (newContractRequest.position() != null){
            contract.setPosition(newContractRequest.position());
        }
        if (newContractRequest.department() != null){
            contract.setDepartment(newContractRequest.department());
        }
        if (newContractRequest.grossAnnualSalary() != null){
            contract.setGrossAnnualSalary(newContractRequest.grossAnnualSalary());
        }
        if (newContractRequest.startDate() != null){
            contract.setStartDate(newContractRequest.startDate());
        }
        if (newContractRequest.endDate() != null){
            contract.setEndDate(newContractRequest.endDate());
        }
        if (newContractRequest.monthlyLeaveDays() != null){
            contract.setMonthlyLeaveDays(newContractRequest.monthlyLeaveDays());
        }
        if (newContractRequest.monthlyLeaveHours() != null){
            contract.setMonthlyLeaveHours(newContractRequest.monthlyLeaveHours());
        }
        if (newContractRequest.monthlyLeaveDays() != null){
            contract.setMonthlyLeaveDays(newContractRequest.monthlyLeaveDays());
        }
        if (newContractRequest.notes() != null){
            contract.setNotes(newContractRequest.notes());
        }
    }
}
