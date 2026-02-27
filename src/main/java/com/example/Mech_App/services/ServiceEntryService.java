package com.example.Mech_App.services;

import com.example.Mech_App.bo.ServiceEntry;
import com.example.Mech_App.models.ServiceEntryFilters;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceEntryService {
    void createServiceEntry(ServiceEntry serviceEntry);

    ServiceEntry getServiceEntry(Long id);

    @Transactional
    void editServiceEntry(Long id, ServiceEntry newData);

    @Transactional
    void deleteServiceEntry(Long id);

    Page<ServiceEntry> getAllServiceEntries(ServiceEntryFilters filters, Pageable pageable);
}
