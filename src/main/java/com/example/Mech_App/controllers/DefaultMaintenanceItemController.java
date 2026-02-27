package com.example.Mech_App.controllers;
import com.example.Mech_App.bo.DefaultMaintenanceItem;
import com.example.Mech_App.models.DefaultMaintenanceItemFilters;
import com.example.Mech_App.services.DefaultMaintenanceItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/default-maintenance-items")
@RequiredArgsConstructor
public class DefaultMaintenanceItemController {

    private final DefaultMaintenanceItemService service;

    @PostMapping("/create")
    public ResponseEntity<DefaultMaintenanceItem> create(@RequestBody DefaultMaintenanceItem item) {
        service.create(item);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DefaultMaintenanceItem> get(@PathVariable Long id) {
        DefaultMaintenanceItem item = service.get(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("all")
    public ResponseEntity<Page<DefaultMaintenanceItem>> getAll(
            DefaultMaintenanceItemFilters filters,
            Pageable pageable
    ) {
        Page<DefaultMaintenanceItem> items = service.getAll(filters, pageable);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Void> edit(@PathVariable Long id, @RequestBody DefaultMaintenanceItem newData) {
        service.edit(id, newData);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
