package aitho.ranim.hrms.utils;

import aitho.ranim.hrms.dto.employeeDto.EmployeeDetailResponse;
import aitho.ranim.hrms.dto.employeeDto.EmployeeRequest;
import aitho.ranim.hrms.dto.employeeDto.EmployeeSummaryResponse;
import aitho.ranim.hrms.dto.employeeDto.UpdateEmployeeRequest;
import aitho.ranim.hrms.entity.Employee;
import lombok.experimental.UtilityClass;


@UtilityClass
public class EmployeeUtils {
    public EmployeeDetailResponse toEmployeeDetailResponse(Employee employee) {
        return new EmployeeDetailResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getDateOfBirth(),
                employee.getGender(),
                employee.getNationality(),
                employee.getBirthPlace(),
                employee.getPhoneNumber(),
                employee.getAddress(),
                employee.getCity(),
                employee.getCountry(),
                employee.getPostalCode(),
                employee.getProvince(),
                employee.getWorkLocation()
        );
    }

    public EmployeeSummaryResponse toEmployeeSummaryResponse(Employee employee) {
        return new EmployeeSummaryResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName()
        );
    }

    public void updateEmployeeFromRequest(Employee employee, UpdateEmployeeRequest newEmployee) {
        if (newEmployee.firstName() != null){
            employee.setFirstName(newEmployee.firstName());
        }
        if (newEmployee.lastName() != null){
            employee.setLastName(newEmployee.lastName());
        }
        if (newEmployee.gender() != null){
            employee.setGender(newEmployee.gender());
        }
        if (newEmployee.dateOfBirth() != null){
            employee.setDateOfBirth(newEmployee.dateOfBirth());
        }
        if (newEmployee.nationality() != null){
            employee.setNationality(newEmployee.nationality());
        }
        if (newEmployee.personalEmail() != null){
            employee.setPersonalEmail(newEmployee.personalEmail());
        }
        if (newEmployee.phoneNumber() != null){
            employee.setPhoneNumber(newEmployee.phoneNumber());
        }
        if(newEmployee.birthPlace() != null){
            employee.setBirthPlace(newEmployee.birthPlace());
        }
        if (newEmployee.address() != null){
            employee.setAddress(newEmployee.address());
        }
        if (newEmployee.city() != null){
            employee.setCity(newEmployee.city());
        }
        if (newEmployee.country() != null){
            employee.setCountry(newEmployee.country());
        }
        if (newEmployee.postalCode() != null){
            employee.setPostalCode(newEmployee.postalCode());
        }
        if (newEmployee.province() != null){
            employee.setProvince(newEmployee.province());
        }
        if (newEmployee.workLocation() != null){
            employee.setWorkLocation(newEmployee.workLocation());
        }
    }

    public Employee createEmployeeFromRequest(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setGender(request.gender());
        employee.setDateOfBirth(request.dateOfBirth());
        employee.setNationality(request.nationality());
        employee.setBirthPlace(request.birthPlace());
        employee.setPhoneNumber(request.phoneNumber());
        employee.setPersonalEmail(request.personalEmail());
        employee.setAddress(request.address());
        employee.setCity(request.city());
        employee.setCountry(request.country());
        employee.setPostalCode(request.postalCode());
        employee.setProvince(request.province());
        employee.setWorkLocation(request.workLocation());

        return employee;
    }
}
