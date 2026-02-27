package com.example.Mech_App.controllers;

import com.example.Mech_App.bo.CarMaintenanceEntry;
import com.example.Mech_App.models.CarMaintenanceEntryFilters;
import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/car-maintenance-entry")
@RequiredArgsConstructor
public class CarMaintenanceEntryController {

    private final ServiceFactory serviceFactory;

    @GetMapping("/get/{id}")
    public ResponseEntity<CarMaintenanceEntry> getEntry(@PathVariable Long id) {
        return ResponseEntity.ok(
                serviceFactory.getCarMaintenanceEntryService()
                        .getCarMaintenanceEntry(id)
        );
    }

    @PostMapping("/create")
    public ResponseEntity<String> createEntry(@RequestBody CarMaintenanceEntry entry) {
        serviceFactory.getCarMaintenanceEntryService()
                .createCarMaintenanceEntry(entry);
        return ResponseEntity.ok("Car maintenance entry created successfully!");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editEntry(
            @PathVariable Long id,
            @RequestBody CarMaintenanceEntry entry
    ) {
        serviceFactory.getCarMaintenanceEntryService()
                .editCarMaintenanceEntry(id, entry);
        return ResponseEntity.ok("Car maintenance entry updated successfully!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEntry(@PathVariable Long id) {
        serviceFactory.getCarMaintenanceEntryService()
                .deleteCarMaintenanceEntry(id);
        return ResponseEntity.ok("Car maintenance entry deleted successfully!");
    }

    @PostMapping("/all")
    public ResponseEntity<Page<CarMaintenanceEntry>> getAllEntries(
            @RequestBody CarMaintenanceEntryFilters filters,
            Pageable pageable
    ) {
        Page<CarMaintenanceEntry> result =
                serviceFactory.getCarMaintenanceEntryService()
                        .getAllCarMaintenanceEntries(filters, pageable);

        return ResponseEntity.ok(result);
    }

}
