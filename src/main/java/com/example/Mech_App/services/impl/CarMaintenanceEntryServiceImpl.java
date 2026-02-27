package com.example.Mech_App.services.impl;

import com.example.Mech_App.services.CarMaintenanceEntryService;

import com.example.Mech_App.bo.CarMaintenanceEntry;
import com.example.Mech_App.models.CarMaintenanceEntryFilters;
import com.example.Mech_App.repositories.CarMaintenanceEntryRepository;
import com.example.Mech_App.specifications.CarMaintenanceEntrySpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarMaintenanceEntryServiceImpl implements CarMaintenanceEntryService {


    private final CarMaintenanceEntryRepository repository;

    @Override
    public void createCarMaintenanceEntry(CarMaintenanceEntry entry) {
        entry.setId(null);
        repository.save(entry);
    }

    @Override
    public CarMaintenanceEntry getCarMaintenanceEntry(Long id) {
        return repository.findByIdRequired(id);
    }

    @Override
    @Transactional
    public void editCarMaintenanceEntry(Long id, CarMaintenanceEntry newData) {

        CarMaintenanceEntry existing = repository.findByIdRequired(id);

        existing.setCarId(newData.getCarId());
        existing.setServiceEntryId(newData.getServiceEntryId());
        existing.setDefaultMaintenanceItemId(newData.getDefaultMaintenanceItemId());
        existing.setDescriptions(newData.getDescriptions());
    }

    @Override
    @Transactional
    public void deleteCarMaintenanceEntry(Long id) {
        repository.findByIdRequired(id);
        repository.deleteById(id);
    }

    @Override
    public Page<CarMaintenanceEntry> getAllCarMaintenanceEntries(
            CarMaintenanceEntryFilters filters,
            Pageable pageable
    ) {
        return repository.findAll(
                CarMaintenanceEntrySpecification.withFilters(
                        null,
                        null,
                        null
                        ),
                pageable
        );
    }

}
