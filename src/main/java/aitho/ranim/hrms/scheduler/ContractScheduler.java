package aitho.ranim.hrms.scheduler;

import aitho.ranim.hrms.entity.Contract;
import aitho.ranim.hrms.repository.IContractRepository;
import aitho.ranim.hrms.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class ContractScheduler {

    private final IContractRepository contractRepository;
    private final EmailService emailService;

    @Value("${app.hr.notification-email}")
    private String hrNotificationEmail;

    @Scheduled(cron = "${app.scheduler.contract-expiry-cron}")
    public void checkExpiringContracts() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(30);

        List<Contract> expiringContracts =
                contractRepository.findByActiveTrueAndEndDateBetween(startDate, endDate);

        log.info("Job contracts: found {} Expiring contracts", expiringContracts.size());

        if (expiringContracts.isEmpty()) {
            return;
        }
        emailService.sendContractExpiryEmail(
                hrNotificationEmail,
                expiringContracts,
                startDate,
                endDate
        );
    }
}
