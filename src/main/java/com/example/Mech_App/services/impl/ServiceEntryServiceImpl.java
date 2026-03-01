package com.example.Mech_App.services.impl;

import com.example.Mech_App.bo.ServiceEntry;
import com.example.Mech_App.models.ServiceEntryFilters;
import com.example.Mech_App.repositories.ServiceEntryRepository;
import com.example.Mech_App.services.ServiceEntryService;
import com.example.Mech_App.specifications.ServiceEntrySpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceEntryServiceImpl implements ServiceEntryService {

    private final ServiceEntryRepository serviceEntryRepository;

    @Override
    public void createServiceEntry(ServiceEntry serviceEntry) {
        serviceEntry.setId(null);
        serviceEntryRepository.save(serviceEntry);
    }

    @Override
    public ServiceEntry getServiceEntry(Long id) {
        return serviceEntryRepository.findByIdRequired(id);
    }

    @Override
    @Transactional
    public void editServiceEntry(Long id, ServiceEntry newData) {

        ServiceEntry existing = serviceEntryRepository.findByIdRequired(id);

        existing.setCarId(newData.getCarId());
        existing.setCustomerId(newData.getCustomerId());
        existing.setEntryDate(newData.getEntryDate());
        existing.setFinishDate(newData.getFinishDate());
        existing.setInitialDescription(newData.getInitialDescription());
        existing.setFinalDescription(newData.getFinalDescription());
        existing.setStatus(newData.getStatus());
        existing.setPartsTotalPrice(newData.getPartsTotalPrice());
        existing.setLaborTotalCost(newData.getLaborTotalCost());
        existing.setOdometer(newData.getOdometer());
    }

    @Override
    @Transactional
    public void deleteServiceEntry(Long id) {
        serviceEntryRepository.findByIdRequired(id);
        serviceEntryRepository.deleteById(id);
    }

    @Override
    public Page<ServiceEntry> getAllServiceEntries(ServiceEntryFilters filters, Pageable pageable) {
        return serviceEntryRepository.findAll(
                ServiceEntrySpecification.withFilters(
                        filters.getCarId(),
                        filters.getCustomerId(),
                        null,
                        null,
                        null
                ),
                pageable
        );
    }


    @Override
    public List<ServiceEntry> getByIds(List<Long> ids) {
        return serviceEntryRepository.findByIdIn(ids);
    }


}
