package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.contractDto.ContractRequest;
import aitho.ranim.hrms.dto.contractDto.ContractResponse;
import aitho.ranim.hrms.dto.contractDto.ContractSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Contract Controller", description = "Endpoints for managing contracts")
public interface IContractController {
    @Operation(summary = "Create Contract", description = "Endpoint to create a new contract. Accessible by ADMIN and HR roles.")
    ResponseEntity<ContractResponse> createContract(@Valid @RequestBody ContractRequest contractRequest);
    ResponseEntity<List<ContractSummaryResponse>> getContractHistory(@PathVariable long employeeId);
    ResponseEntity<ContractResponse> getActiveContract(@PathVariable long employeeId);
    ResponseEntity<ContractResponse> updateContract(@PathVariable long id, @Valid @RequestBody ContractRequest contractRequest);
    void activateContract(@PathVariable long id);

}
