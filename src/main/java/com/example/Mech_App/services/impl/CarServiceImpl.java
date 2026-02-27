package com.example.Mech_App.services.impl;

import com.example.Mech_App.bo.Car;
import com.example.Mech_App.models.CarFilters;
import com.example.Mech_App.repositories.CarRepository;
import com.example.Mech_App.services.CarService;
import com.example.Mech_App.specifications.CarSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Override
    public void createCar(Car car) {
        car.setId(null);
        carRepository.save(car);
    }

    @Override
    public Car getCar(Long id) {
        return carRepository.findByIdRequired(id);
    }

    @Override
    @Transactional
    public void editCar(Long id, Car newCarData) {
        Car existingCar = carRepository.findByIdRequired(id);

        existingCar.setClientId(newCarData.getClientId());
        existingCar.setMark(newCarData.getMark());
        existingCar.setModel(newCarData.getModel());
        existingCar.setYear(newCarData.getYear());
        existingCar.setLicenseNo(newCarData.getLicenseNo());
        existingCar.setVin(newCarData.getVin());
        existingCar.setFuel(newCarData.getFuel());
        existingCar.setCarossery(newCarData.getCarossery());
        existingCar.setEngine(newCarData.getEngine());
        existingCar.setColor(newCarData.getColor());
        existingCar.setTransmission(newCarData.getTransmission());
        existingCar.setCarType(newCarData.getCarType());
    }

    @Override
    @Transactional
    public void deleteCar(Long id) {
        carRepository.findByIdRequired(id);
        carRepository.deleteById(id);
    }

    @Override
    public Page<Car> getAllCars(CarFilters filters, Pageable pageable) {
        return carRepository.findAll(
                CarSpecification.withFilters(
                        null,
                        null,
                        filters.getLicenseNo(),
                        null
                ),
                pageable
        );
    }


}
