package com.example.Mech_App.controllers;

import com.example.Mech_App.bo.CarMaintenanceEntry;
import com.example.Mech_App.models.CarMaintenanceEntryComplete;
import com.example.Mech_App.models.CarMaintenanceEntryFilters;
import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getLastChangesByCar/{carId}")
    public ResponseEntity<List<CarMaintenanceEntryComplete>> getLastChangesByCar(@PathVariable("carId") Long carId) {
        return ResponseEntity.ok(
                serviceFactory.getCarMaintenanceEntryService().findLatestChangesByCar(carId)
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

    @GetMapping("/by-service-entry/{serviceEntryId}")
    public ResponseEntity<List<CarMaintenanceEntry>> getByServiceEntry(@PathVariable Long serviceEntryId) {
        CarMaintenanceEntryFilters filters = CarMaintenanceEntryFilters.builder()
                .serviceEntryId(serviceEntryId)
                .build();
        Page<CarMaintenanceEntry> page = serviceFactory.getCarMaintenanceEntryService()
                .getAllCarMaintenanceEntries(filters, PageRequest.of(0, 1000));
        return ResponseEntity.ok(page.getContent());
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
