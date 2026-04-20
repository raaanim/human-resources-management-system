package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.EmployeeRequest;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.service.IEmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }


    @Override
    public void sendActivationEmail(Employee employee, String activationLink) {
        try {
            Context context = new Context();
            context.setVariable("name", employee.getFirstName());
            context.setVariable("surname", employee.getLastName());
            context.setVariable("activationLink", activationLink);

            String html = templateEngine.process("activation-email", context);

            sendHtml(employee.getEmail(), "Account activation", html);

        } catch (Exception e) {
            System.err.println("Error sending activation email: " + e.getMessage());
        }
    }

    @Override
    public void sendWelcomeEmail(Employee employee) {
        try {
            Context context = new Context();
            context.setVariable("name", employee.getFirstName());
            context.setVariable("surname", employee.getLastName());

            String html = templateEngine.process("welcome-email", context);

            sendHtml(employee.getEmail(), "Welcome", html);

        } catch (Exception e) {
            System.err.println("Error sending welcome email: " + e.getMessage());
        }
    }

    private void sendHtml(String to, String subject, String html) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            System.err.println("Error sending email to " + to + ": " + e.getMessage());
        }
    }
}
