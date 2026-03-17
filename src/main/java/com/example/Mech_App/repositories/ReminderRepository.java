package com.example.Mech_App.repositories;

import com.example.Mech_App.bo.Remainder;
import com.example.Mech_App.configs.CustomResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends JpaRepository<Remainder, Long> {

    default Remainder findByIdRequired(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Remainder not found with id: " + id));
    }

    Page<Remainder> findAllByClientIdOrderByIdDesc(Long clientId, Pageable pageable);

    Page<Remainder> findAllBySawByAdminOrderByIdDesc(Boolean sawByAdmin, Pageable pageable);

    Page<Remainder> findAllByOrderByIdDesc(Pageable pageable);

    void deleteByCarId(Long carId);

    void deleteByCarMaintenanceEntry(Long carMaintenanceEntryId);
}
