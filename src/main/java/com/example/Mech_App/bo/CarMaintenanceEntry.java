package com.example.Mech_App.bo;

import com.example.Mech_App.utils.Audit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class CarMaintenanceEntry extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long carId;
    Long serviceEntryId;
    Long defaultMaintenanceItemId;
    String descriptions;
}
