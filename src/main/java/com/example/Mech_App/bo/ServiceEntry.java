package com.example.Mech_App.bo;

import com.example.Mech_App.enums.ServiceStatus;
import com.example.Mech_App.utils.Audit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class ServiceEntry extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long carId;
    Long customerId;
    LocalDateTime entryDate;
    LocalDateTime finishDate;
    @Column(columnDefinition = "TEXT")
    String initialDescription;
    @Column(columnDefinition = "TEXT")
    String finalDescription;
    @Enumerated(EnumType.STRING)
    ServiceStatus status;
    Long partsTotalPrice;
    Long laborTotalCost;

}
