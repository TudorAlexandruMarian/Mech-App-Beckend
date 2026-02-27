package com.example.Mech_App.repositories;

import com.example.Mech_App.bo.Car;
import com.example.Mech_App.configs.CustomResponseStatusException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    default Car findByIdRequired(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Car not found with id: " + id));
    }

    List<Car> findByClientId(Long clientId);


}
