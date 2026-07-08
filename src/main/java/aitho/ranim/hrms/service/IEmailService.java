package aitho.ranim.hrms.service;

import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveRequest;

public interface IEmailService {
    public void sendActivationEmail(Employee employee, String activationLink);

    public void sendWelcomeEmail(Employee employee);
    void sendLeaveRequestSubmitted(LeaveRequest leaveRequest);
}
