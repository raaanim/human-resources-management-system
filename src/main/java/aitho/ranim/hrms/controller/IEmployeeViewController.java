package aitho.ranim.hrms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(
        name = "Employee View Controller",
        description = "MVC endpoints for employee view operations (server-side rendered pages)"
)
public interface IEmployeeViewController {
    @Operation(
            summary = "Activate Employee",
            description = "Activates an employee account using the provided token and returns a server-side rendered view."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee activated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    String activateEmployee(@PathVariable String token, Model model);
}
