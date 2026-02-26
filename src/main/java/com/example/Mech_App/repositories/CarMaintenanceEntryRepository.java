package com.example.Mech_App.repositories;

import com.example.Mech_App.bo.CarMaintenanceEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarMaintenanceEntryRepository extends JpaRepository<CarMaintenanceEntry, Long> {
}
