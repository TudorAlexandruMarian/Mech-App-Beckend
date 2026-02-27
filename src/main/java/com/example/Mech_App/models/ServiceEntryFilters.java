package com.example.Mech_App.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceEntryFilters {
    LocalDate date;
    Long carId;
    Long customerId;
}
