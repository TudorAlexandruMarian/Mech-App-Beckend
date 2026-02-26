package com.example.Mech_App.repositories;

import com.example.Mech_App.bo.DefaultMaintenanceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultMaintenanceItemRepository extends JpaRepository<DefaultMaintenanceItem, Long> {
}
