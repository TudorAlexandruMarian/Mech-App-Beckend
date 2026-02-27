package com.example.Mech_App.services;

import com.example.Mech_App.bo.Car;
import com.example.Mech_App.models.CarFilters;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarService {


    void createCar(Car car);

    Car getCar(Long id);

    @Transactional
    void editCar(Long id, Car newCarData);

    @Transactional
    void deleteCar(Long id);

    Page<Car> getAllCars(CarFilters filters, Pageable pageable);

    List<Car> getAllCarsByCustomer(Long clientId);
}
