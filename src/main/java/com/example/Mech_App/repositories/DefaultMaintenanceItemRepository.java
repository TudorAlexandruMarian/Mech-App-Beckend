package com.example.Mech_App.repositories;

import com.example.Mech_App.bo.Car;
import com.example.Mech_App.bo.DefaultMaintenanceItem;
import com.example.Mech_App.configs.CustomResponseStatusException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefaultMaintenanceItemRepository extends JpaRepository<DefaultMaintenanceItem, Long>, JpaSpecificationExecutor<DefaultMaintenanceItem> {

    default DefaultMaintenanceItem findByIdRequired(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "DefaultMaintenanceItem not found with id: " + id));
    }

    List<DefaultMaintenanceItem> findByIdIn(List<Long> ids);

}
