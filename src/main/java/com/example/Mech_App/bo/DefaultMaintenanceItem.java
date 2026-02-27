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
public class DefaultMaintenanceItem extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    Integer minLifeTime;
    Integer maxLifeTime;
    Long minLifeKm;
    Long maxLifeKm;
}
