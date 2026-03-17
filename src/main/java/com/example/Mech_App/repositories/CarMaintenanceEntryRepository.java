package com.example.Mech_App.repositories;

import com.example.Mech_App.bo.CarMaintenanceEntry;
import com.example.Mech_App.configs.CustomResponseStatusException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarMaintenanceEntryRepository extends JpaRepository<CarMaintenanceEntry, Long>, JpaSpecificationExecutor<CarMaintenanceEntry> {

    default CarMaintenanceEntry findByIdRequired(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "CarMaintenanceEntry not found with id: " + id));
    }

    @Query("""
             SELECT c FROM CarMaintenanceEntry c
             WHERE c.id IN (
                 SELECT MAX(sub.id)
                 FROM CarMaintenanceEntry sub
                 WHERE sub.carId = :carId
                 GROUP BY sub.defaultMaintenanceItemId
             )
            """)
    List<CarMaintenanceEntry> findLatestDistinctEntries(
            @Param("carId") Long carId
    );

    void deleteByCarId(Long carId);

    void deleteByServiceEntryIdIn(List<Long> serviceEntryIds);

    void deleteByDefaultMaintenanceItemId(Long defaultMaintenanceItemId);
}
