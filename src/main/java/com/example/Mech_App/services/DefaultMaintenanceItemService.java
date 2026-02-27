package com.example.Mech_App.services;

import com.example.Mech_App.bo.DefaultMaintenanceItem;
import com.example.Mech_App.models.DefaultMaintenanceItemFilters;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DefaultMaintenanceItemService {
    void create(DefaultMaintenanceItem item);

    DefaultMaintenanceItem get(Long id);

    @Transactional
    void edit(Long id, DefaultMaintenanceItem newData);

    @Transactional
    void delete(Long id);

    Page<DefaultMaintenanceItem> getAll(
            DefaultMaintenanceItemFilters filters,
            Pageable pageable
    );
}
