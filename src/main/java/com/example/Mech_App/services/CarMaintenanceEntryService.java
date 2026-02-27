package com.example.Mech_App.services;

import com.example.Mech_App.bo.CarMaintenanceEntry;
import com.example.Mech_App.models.CarMaintenanceEntryFilters;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarMaintenanceEntryService {
    void createCarMaintenanceEntry(CarMaintenanceEntry entry);

    CarMaintenanceEntry getCarMaintenanceEntry(Long id);

    @Transactional
    void editCarMaintenanceEntry(Long id, CarMaintenanceEntry newData);

    @Transactional
    void deleteCarMaintenanceEntry(Long id);

    Page<CarMaintenanceEntry> getAllCarMaintenanceEntries(
            CarMaintenanceEntryFilters filters,
            Pageable pageable
    );
}
