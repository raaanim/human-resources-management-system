package aitho.ranim.hrms.utils;

import aitho.ranim.hrms.dto.*;
import aitho.ranim.hrms.entity.Employee;
import lombok.experimental.UtilityClass;

import java.util.UUID;

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
        employee.setFirstName(newEmployee.firstName());
        employee.setLastName(newEmployee.lastName());
        employee.setGender(newEmployee.gender());
        employee.setDateOfBirth(newEmployee.dateOfBirth());
        employee.setNationality(newEmployee.nationality());
        employee.setBirthPlace(newEmployee.birthPlace());
        employee.setPersonalEmail(newEmployee.personalEmail());
        employee.setPhoneNumber(newEmployee.phoneNumber());
        employee.setAddress(newEmployee.address());
        employee.setCity(newEmployee.city());
        employee.setCountry(newEmployee.country());
        employee.setPostalCode(newEmployee.postalCode());
        employee.setProvince(newEmployee.province());
        employee.setWorkLocation(newEmployee.workLocation());
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
