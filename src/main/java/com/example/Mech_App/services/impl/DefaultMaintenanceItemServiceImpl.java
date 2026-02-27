package com.example.Mech_App.services.impl;

import com.example.Mech_App.services.DefaultMaintenanceItemService;

import com.example.Mech_App.bo.DefaultMaintenanceItem;
import com.example.Mech_App.models.DefaultMaintenanceItemFilters;
import com.example.Mech_App.repositories.DefaultMaintenanceItemRepository;
import com.example.Mech_App.specifications.DefaultMaintenanceItemSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultMaintenanceItemServiceImpl implements DefaultMaintenanceItemService {


    private final DefaultMaintenanceItemRepository repository;

    @Override
    public void create(DefaultMaintenanceItem item) {
        item.setId(null);
        repository.save(item);
    }

    @Override
    public DefaultMaintenanceItem get(Long id) {
        return repository.findByIdRequired(id);
    }

    @Override
    @Transactional
    public void edit(Long id, DefaultMaintenanceItem newData) {

        DefaultMaintenanceItem existing = repository.findByIdRequired(id);

        existing.setName(newData.getName());
        existing.setMinLifeTime(newData.getMinLifeTime());
        existing.setMaxLifeTime(newData.getMaxLifeTime());
        existing.setMinLifeKm(newData.getMinLifeKm());
        existing.setMaxLifeKm(newData.getMaxLifeKm());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.findByIdRequired(id);
        repository.deleteById(id);
    }

    @Override
    public Page<DefaultMaintenanceItem> getAll(
            DefaultMaintenanceItemFilters filters,
            Pageable pageable
    ) {
        return repository.findAll(
                DefaultMaintenanceItemSpecification.withFilters(
                        null,
                        null,
                        null
                ),
                pageable
        );
    }

}
