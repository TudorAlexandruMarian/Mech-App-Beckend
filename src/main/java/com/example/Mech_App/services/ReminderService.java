package com.example.Mech_App.services;

import com.example.Mech_App.bo.Remainder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReminderService {
    void createReminders();

    Page<Remainder> findAllRemindersByClientId(Long clientId, Pageable pageable);

    Page<Remainder> findAllRemindersForAdmin(Pageable pageable);

    Page<Remainder> findAllRemindersForAdminNew(Pageable pageable);

    void updateReminderStatusByAdmin(Long reminderId);

    void updateReminderStatusByClient(Long reminderId);
}
