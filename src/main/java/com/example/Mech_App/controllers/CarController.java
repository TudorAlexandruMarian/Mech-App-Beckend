package com.example.Mech_App.controllers;

import com.example.Mech_App.bo.Car;
import com.example.Mech_App.models.CarFilters;
import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {

    private final ServiceFactory serviceFactory;

    @GetMapping("/get/{id}")
    public ResponseEntity<Car> getCar(@PathVariable Long id) {
        return ResponseEntity.ok(
                serviceFactory.getCarService().getCar(id)
        );
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCar(@RequestBody Car car) {
        serviceFactory.getCarService().createCar(car);
        return ResponseEntity.ok("Car created successfully!");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editCar(
            @PathVariable Long id,
            @RequestBody Car car
    ) {
        serviceFactory.getCarService().editCar(id, car);
        return ResponseEntity.ok("Car updated successfully!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        serviceFactory.getCarService().deleteCar(id);
        return ResponseEntity.ok("Car deleted successfully!");
    }

    @PostMapping("/all")
    public ResponseEntity<Page<Car>> getAllCars(
            @RequestBody CarFilters filters,
            Pageable pageable
    ) {
        Page<Car> cars = serviceFactory
                .getCarService()
                .getAllCars(filters, pageable);

        return ResponseEntity.ok(cars);
    }

}
