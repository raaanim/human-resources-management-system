package aitho.ranim.hrms.service;


import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeResponse;
<<<<<<< HEAD
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.viewmodel.EmployeeViewModel;
import jakarta.validation.Valid;
=======
import aitho.ranim.hrms.viewmodel.EmployeeViewModel;
>>>>>>> c79a468 (refactor: improve email service and cleanup imports)

public interface IEmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeViewModel activateEmployee(String token);
}
