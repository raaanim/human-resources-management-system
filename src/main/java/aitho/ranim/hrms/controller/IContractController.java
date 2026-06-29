package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.contractDto.ContractRequest;
import aitho.ranim.hrms.dto.contractDto.ContractResponse;
import aitho.ranim.hrms.dto.contractDto.ContractSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Contract Controller", description = "Endpoints for managing contracts")
public interface IContractController {
    @Operation(
            summary = "Create Contract",
            description = "Creates a new contract for an employee."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Contract successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    ResponseEntity<ContractResponse> createContract(@Valid @RequestBody ContractRequest contractRequest);
    @Operation(
            summary = "Get Contract History",
            description = "Retrieves the complete contract history of a specific employee."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contract history retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    ResponseEntity<List<ContractSummaryResponse>> getContractHistory(@PathVariable long employeeId);
    @Operation(
            summary = "Get Active Contract",
            description = "Retrieves the currently active contract for a specific employee."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Active contract successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee or active contract not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    ResponseEntity<ContractResponse> getActiveContract(@PathVariable long employeeId);
    @Operation(
            summary = "Update Contract",
            description = "Updates an existing contract."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contract updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Contract not found")
    })
    ResponseEntity<ContractResponse> updateContract(@PathVariable long id, @Valid @RequestBody ContractRequest contractRequest);
    @Operation(
            summary = "Activate Contract",
            description = "Activates the selected contract and deactivates all other contracts belonging to the same employee."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contract activated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Contract not found")
    })
    void activateContract(@PathVariable long id);

}
