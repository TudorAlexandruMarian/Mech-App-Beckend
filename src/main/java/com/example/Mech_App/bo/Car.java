package com.example.Mech_App.bo;

import com.example.Mech_App.enums.FuelType;
import com.example.Mech_App.enums.TransmissionType;
import com.example.Mech_App.utils.Audit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Car extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long clientId;
    String mark;
    String model;
    String year;
    String licenseNo;
    String vin;
    @Enumerated(EnumType.STRING)
    FuelType fuel;
    String carossery;
    String engine;
    String color;
    @Enumerated(EnumType.STRING)
    TransmissionType transmission;
    @Enumerated(EnumType.STRING)
    TransmissionType carType;


}
