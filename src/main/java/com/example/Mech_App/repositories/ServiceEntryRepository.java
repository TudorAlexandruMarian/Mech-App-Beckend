package com.example.Mech_App.repositories;

import com.example.Mech_App.bo.ServiceEntry;
import com.example.Mech_App.configs.CustomResponseStatusException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceEntryRepository extends JpaRepository<ServiceEntry, Long>, JpaSpecificationExecutor<ServiceEntry> {

    default ServiceEntry findByIdRequired(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "ServiceEntry not found with id: " + id));
    }
    Optional<ServiceEntry> findTopByCarIdOrderByFinishDateDesc(Long carId);
    List<ServiceEntry> findByIdIn(List<Long> ids);

    List<ServiceEntry> findByCarId(Long carId);
}
