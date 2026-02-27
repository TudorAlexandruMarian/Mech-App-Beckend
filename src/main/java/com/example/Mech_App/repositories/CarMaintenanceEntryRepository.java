package com.example.Mech_App.repositories;

import com.example.Mech_App.bo.CarMaintenanceEntry;
import com.example.Mech_App.bo.DefaultMaintenanceItem;
import com.example.Mech_App.configs.CustomResponseStatusException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface CarMaintenanceEntryRepository extends JpaRepository<CarMaintenanceEntry, Long>, JpaSpecificationExecutor<CarMaintenanceEntry> {

    default CarMaintenanceEntry findByIdRequired(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "CarMaintenanceEntry not found with id: " + id));
    }
}
