package com.example.Mech_App.models;


import com.example.Mech_App.bo.CarMaintenanceEntry;
import com.example.Mech_App.bo.DefaultMaintenanceItem;
import com.example.Mech_App.bo.ServiceEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarLastChangesForRemainder {

    List<ItemToCheck> itemsToCheck;

    ServiceEntry lastServiceEntry;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ItemToCheck{
        CarMaintenanceEntry carMaintenanceEntry;
        DefaultMaintenanceItem defaultMaintenanceItem;
    }
}
