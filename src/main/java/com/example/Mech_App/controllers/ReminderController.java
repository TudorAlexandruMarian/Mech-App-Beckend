package com.example.Mech_App.controllers;

import com.example.Mech_App.bo.Remainder;
import com.example.Mech_App.services.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reminder")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<Remainder>> getRemindersForClient(
            @PathVariable Long clientId,
            Pageable pageable
    ) {
        Page<Remainder> result = reminderService.findAllRemindersByClientId(clientId, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<Remainder>> getRemindersForAdmin(
            @RequestParam(name = "onlyNew", defaultValue = "false") boolean onlyNew,
            Pageable pageable
    ) {
        Page<Remainder> result = onlyNew
                ? reminderService.findAllRemindersForAdminNew(pageable)
                : reminderService.findAllRemindersForAdmin(pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/mark-read/admin/{id}")
    public ResponseEntity<Void> markAsReadByAdmin(@PathVariable Long id) {
        reminderService.updateReminderStatusByAdmin(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mark-read/client/{id}")
    public ResponseEntity<Void> markAsReadByClient(@PathVariable Long id) {
        reminderService.updateReminderStatusByClient(id);
        return ResponseEntity.ok().build();
    }
}

