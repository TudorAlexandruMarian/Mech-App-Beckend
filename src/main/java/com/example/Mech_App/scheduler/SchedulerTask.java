package com.example.Mech_App.scheduler;

import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerTask {

    private final ServiceFactory serviceFactory;
    @Scheduled(fixedRate = 600000) // every 60 seconds
    public void checkReminders() {
        serviceFactory.getReminderService().createReminders();
    }

}
