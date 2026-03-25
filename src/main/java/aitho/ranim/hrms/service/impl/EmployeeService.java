package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.dto.EmployeeResponse;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final IEmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee  = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setPassword(request.password());
        employee.setDateOfBirth(LocalDate.parse(request.dateOfBirth()));
        employee.setGender(request.gender());
        employee.setCity(request.city());
        employee.setCountry(request.country());
        employee.setNationality(request.nationality());
        employee.setBirthPlace(request.birthPlace());
        employee.setPhoneNumber(request.phoneNumber());
        employee.setWorkEmail(request.workEmail());
        employee.setPersonalEmail(request.personalEmail());
        employee.setAddress(request.address());
        employee.setPostalCode(request.postalCode());
        employee.setProvince(request.province());
        employee.setWorkLocation(request.workLocation());
        employee.setStatus(request.status());

        Employee savedEmployee = employeeRepository.save(employee);
        return new EmployeeResponse(
                savedEmployee.getId(),
                savedEmployee.getFirstName(),
                savedEmployee.getLastName(),
                savedEmployee.getDateOfBirth(),
                java.time.LocalDate.now()


        );
    }



}
