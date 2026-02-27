package com.example.Mech_App.controllers;

import com.example.Mech_App.bo.ServiceEntry;
import com.example.Mech_App.models.ServiceEntryFilters;
import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service-entry")
@RequiredArgsConstructor
public class ServiceEntryController {

    private final ServiceFactory serviceFactory;

    @GetMapping("/get/{id}")
    public ResponseEntity<ServiceEntry> getServiceEntry(@PathVariable Long id) {
        return ResponseEntity.ok(
                serviceFactory.getServiceEntryService().getServiceEntry(id)
        );
    }

    @PostMapping("/create")
    public ResponseEntity<String> createServiceEntry(@RequestBody ServiceEntry serviceEntry) {
        serviceFactory.getServiceEntryService().createServiceEntry(serviceEntry);
        return ResponseEntity.ok("Service entry created successfully!");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editServiceEntry(
            @PathVariable Long id,
            @RequestBody ServiceEntry serviceEntry
    ) {
        serviceFactory.getServiceEntryService().editServiceEntry(id, serviceEntry);
        return ResponseEntity.ok("Service entry updated successfully!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteServiceEntry(@PathVariable Long id) {
        serviceFactory.getServiceEntryService().deleteServiceEntry(id);
        return ResponseEntity.ok("Service entry deleted successfully!");
    }

    @PostMapping("/all")
    public ResponseEntity<Page<ServiceEntry>> getAllServiceEntries(
            @RequestBody ServiceEntryFilters filters,
            Pageable pageable
    ) {
        Page<ServiceEntry> result = serviceFactory.getServiceEntryService().getAllServiceEntries(filters, pageable);
        return ResponseEntity.ok(result);
    }

}
