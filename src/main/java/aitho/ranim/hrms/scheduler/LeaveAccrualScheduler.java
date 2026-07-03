package aitho.ranim.hrms.scheduler;

import aitho.ranim.hrms.service.impl.LeaveAccrualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class LeaveAccrualScheduler {
    private final LeaveAccrualService leaveAccrualService;;

    @Scheduled(cron = "${leave.accrual.cron}")
    public void run() {
        leaveAccrualService.processMonthlyAccrual();
    }
}
