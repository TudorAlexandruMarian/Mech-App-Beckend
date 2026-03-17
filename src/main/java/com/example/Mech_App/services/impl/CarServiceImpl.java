package com.example.Mech_App.services.impl;

import com.example.Mech_App.bo.Car;
import com.example.Mech_App.models.CarFilters;
import com.example.Mech_App.repositories.CarMaintenanceEntryRepository;
import com.example.Mech_App.repositories.CarRepository;
import com.example.Mech_App.repositories.ReminderRepository;
import com.example.Mech_App.repositories.ServiceEntryRepository;
import com.example.Mech_App.services.CarService;
import com.example.Mech_App.specifications.CarSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ServiceEntryRepository serviceEntryRepository;
    private final CarMaintenanceEntryRepository carMaintenanceEntryRepository;
    private final ReminderRepository reminderRepository;

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
        // Ensure car exists
        carRepository.findByIdRequired(id);

        // 1) Load all service entries for this car
        var serviceEntries = serviceEntryRepository.findByCarId(id);
        var serviceEntryIds = serviceEntries.stream()
                .map(se -> se.getId())
                .toList();

        // 2) Delete all car maintenance entries linked to this car or to its service entries
        if (!serviceEntryIds.isEmpty()) {
            carMaintenanceEntryRepository.deleteByServiceEntryIdIn(serviceEntryIds);
        }
        carMaintenanceEntryRepository.deleteByCarId(id);

        // 3) Delete all reminders linked directly to this car
        reminderRepository.deleteByCarId(id);

        // 4) Finally delete the car itself
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

    @Override
    public List<Car> getAll() {
        return carRepository.findAll();
    }


    @Override
    public List<Car> getAllCarsByCustomer(Long clientId) {
        return carRepository.findByClientId(clientId);
    }


}
