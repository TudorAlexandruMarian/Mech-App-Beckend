package com.example.Mech_App.bo;

import com.example.Mech_App.utils.Audit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Remainder extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long carMaintenanceEntry;
    Long carId;
    Long clientId;
    String message;
    Boolean sawByClient;
    Boolean sawByAdmin;
}
