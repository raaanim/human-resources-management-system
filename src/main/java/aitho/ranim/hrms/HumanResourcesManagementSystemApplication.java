package aitho.ranim.hrms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HumanResourcesManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HumanResourcesManagementSystemApplication.class, args);
    }

}
