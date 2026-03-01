package com.example.Mech_App.models;

import com.example.Mech_App.bo.CarMaintenanceEntry;
import com.example.Mech_App.bo.DefaultMaintenanceItem;
import com.example.Mech_App.bo.ServiceEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarMaintenanceEntryComplete {
    CarMaintenanceEntry carMaintenanceEntry;
    DefaultMaintenanceItem defaultMaintenanceItem;
    ServiceEntry serviceEntry;
}
