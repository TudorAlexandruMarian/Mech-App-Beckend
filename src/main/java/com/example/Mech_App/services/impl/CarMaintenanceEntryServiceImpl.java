package com.example.Mech_App.services.impl;

import com.example.Mech_App.bo.DefaultMaintenanceItem;
import com.example.Mech_App.bo.ServiceEntry;
import com.example.Mech_App.models.CarMaintenanceEntryComplete;
import com.example.Mech_App.services.CarMaintenanceEntryService;

import com.example.Mech_App.bo.CarMaintenanceEntry;
import com.example.Mech_App.models.CarMaintenanceEntryFilters;
import com.example.Mech_App.repositories.CarMaintenanceEntryRepository;
import com.example.Mech_App.services.ServiceFactory;
import com.example.Mech_App.specifications.CarMaintenanceEntrySpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarMaintenanceEntryServiceImpl implements CarMaintenanceEntryService {


    private final CarMaintenanceEntryRepository repository;

    private final ServiceFactory serviceFactory;

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
    public List<CarMaintenanceEntryComplete> findLatestChangesByCar(Long carId) {
        List<CarMaintenanceEntryComplete> response = new ArrayList<>();

        List<CarMaintenanceEntry> lastChanges = repository.findLatestDistinctEntries(carId);

        List<Long> defaultMaintenanceItemIds = lastChanges.stream()
                .map(CarMaintenanceEntry::getDefaultMaintenanceItemId)
                .collect(Collectors.toList());

        List<Long> allServiceEntriesIds = lastChanges.stream()
                .map(CarMaintenanceEntry::getServiceEntryId)
                .toList();

        List<DefaultMaintenanceItem> allItemsByIds = serviceFactory.getDefaultMaintenanceItemService().findByIdIn(defaultMaintenanceItemIds);
        Map<Long, DefaultMaintenanceItem> itemsById = allItemsByIds.stream()
                .collect(Collectors.toMap(DefaultMaintenanceItem::getId, item -> item));

        List<ServiceEntry> allServicesByIds = serviceFactory.getServiceEntryService().getByIds(allServiceEntriesIds);
        Map<Long, ServiceEntry> servicesById = allServicesByIds.stream()
                .collect(Collectors.toMap(ServiceEntry::getId, item -> item));
        for (CarMaintenanceEntry lastMaintenanceItem : lastChanges) {
            response.add(CarMaintenanceEntryComplete.builder()
                    .carMaintenanceEntry(lastMaintenanceItem)
                    .defaultMaintenanceItem(itemsById.getOrDefault(lastMaintenanceItem.getDefaultMaintenanceItemId(), null))
                    .serviceEntry(servicesById.getOrDefault(lastMaintenanceItem.getServiceEntryId(), null))
                    .build());
        }

        return response;
    }

    @Override
    public Page<CarMaintenanceEntry> getAllCarMaintenanceEntries(
            CarMaintenanceEntryFilters filters,
            Pageable pageable
    ) {
        return repository.findAll(
                CarMaintenanceEntrySpecification.withFilters(
                        null,
                        filters.getServiceEntryId(),
                        null
                ),
                pageable
        );
    }

}
