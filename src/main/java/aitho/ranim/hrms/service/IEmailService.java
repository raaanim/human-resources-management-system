package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.entity.Employee;

public interface IEmailService {
    public void sendActivationEmail(Employee employee, String activationLink);

    public void sendWelcomeEmail(Employee employee);
}
