package aitho.ranim.hrms.service;

import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.service.impl.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendActivationEmail() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@email.com");

        String link = "http://test-link";

        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("activation-email"), any(Context.class)))
                .thenReturn("<html>Activation</html>");

        emailService.sendActivationEmail(employee, link);

        verify(templateEngine, times(1))
                .process(eq("activation-email"), any(Context.class));

        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void shouldSendWelcomeEmail() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@email.com");

        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("welcome-email"), any(Context.class)))
                .thenReturn("<html>Welcome</html>");

        emailService.sendWelcomeEmail(employee);

        verify(templateEngine, times(1))
                .process(eq("welcome-email"), any(Context.class));

        verify(javaMailSender, times(1)).send(mimeMessage);
    }
}
