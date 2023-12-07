package com.example.marketplace.service.schedulers;


import com.example.marketplace.service.UserActivationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ActivationLinkCleanupScheduler {
    private final UserActivationService userActivationService;

    public ActivationLinkCleanupScheduler(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUp() {
        userActivationService.cleanUpObsoleteActivationLinks();
    }
}
