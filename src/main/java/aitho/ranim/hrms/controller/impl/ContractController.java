package aitho.ranim.hrms.controller.impl;

import aitho.ranim.hrms.controller.IContractController;
import aitho.ranim.hrms.dto.contractDto.ContractRequest;
import aitho.ranim.hrms.dto.contractDto.ContractResponse;
import aitho.ranim.hrms.dto.contractDto.ContractSummaryResponse;
import aitho.ranim.hrms.service.IContractService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contract")
public class ContractController implements IContractController {

    private final IContractService contractService;

    public ContractController(IContractService contractService) {
        this.contractService = contractService;
    }

    // POST path = http://localhost:8080/api/v1/contract
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PostMapping
    public ResponseEntity<ContractResponse> createContract(@Valid @RequestBody ContractRequest contractRequest) {
        ContractResponse response = contractService.createContract(contractRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET path = http://localhost:8080/api/v1/contract/employee/{employeeId}
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ContractSummaryResponse>> getContractHistory(@PathVariable long employeeId) {
        List<ContractSummaryResponse> response = contractService.getEmployeeContractDetails(employeeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET path = http://localhost:8080/api/v1/contract/employee/{employeeId}/active
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/employee/{employeeId}/active")
    public ResponseEntity<ContractResponse> getActiveContract(@PathVariable long employeeId) {
        ContractResponse response = contractService.getActiveContract(employeeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PUT path = http://localhost:8080/api/v1/contract/update/{id}
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ContractResponse> updateContract(@PathVariable long id, @Valid @RequestBody ContractRequest contractRequest) {
        ContractResponse response = contractService.updateContract(contractRequest, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PUT path = http://localhost:8080/api/v1/contract/activate/{id}
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PutMapping("/activate/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateContract(@PathVariable long id){
        contractService.activateContract(id);
    }
}
