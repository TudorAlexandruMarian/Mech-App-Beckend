package com.example.Mech_App.scheduler;

import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerTask {

    private final ServiceFactory serviceFactory;
    @Scheduled(fixedRate = 21600000) // every 6 hours
    public void checkReminders() {
        serviceFactory.getReminderService().createReminders();
    }

}
