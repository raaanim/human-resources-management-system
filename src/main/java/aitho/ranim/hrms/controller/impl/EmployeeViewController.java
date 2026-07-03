package aitho.ranim.hrms.controller.impl;


import aitho.ranim.hrms.controller.IEmployeeViewController;

import aitho.ranim.hrms.service.IEmployeeService;
import aitho.ranim.hrms.viewmodel.EmployeeViewModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Tag(name = "Employee View Controller", description = "Endpoints for employee view operations")
@RequestMapping("/view/v1/employee")
public class EmployeeViewController implements IEmployeeViewController {
    private final IEmployeeService employeeService;

    public EmployeeViewController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //GET path = http://localhost:8080/api/v1/employee/activate/{token}
    @GetMapping("/activate/{token}")
    @Override
    @Operation(summary = "Activate Employee", description = "Activates an employee account using the provided token.")
    @ApiResponse(responseCode = "200", description = "Employee activated successfully")
    public String activateEmployee(@PathVariable String token, Model model) {
        EmployeeViewModel viewModel = employeeService.activateEmployee(token);

        model.addAttribute("employee", viewModel);
        return "welcome-email";
    }
}
