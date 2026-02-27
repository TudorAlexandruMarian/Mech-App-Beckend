package com.example.Mech_App.services.impl;

import com.example.Mech_App.models.CountsReport;
import com.example.Mech_App.repositories.CarRepository;
import com.example.Mech_App.repositories.ClientRepository;
import com.example.Mech_App.repositories.DefaultMaintenanceItemRepository;
import com.example.Mech_App.repositories.ServiceEntryRepository;
import com.example.Mech_App.services.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {

    private final CarRepository carRepository;
    private final ClientRepository clientRepository;
    private final ServiceEntryRepository serviceEntryRepository;
    private final DefaultMaintenanceItemRepository defaultMaintenanceItemRepository;


    @Override
    public CountsReport getCounts() {
        return CountsReport.builder()
                .cars(carRepository.count())
                .clients(clientRepository.count())
                .serviceEntries(serviceEntryRepository.count())
                .maintenanceItem(defaultMaintenanceItemRepository.count())
                .build();
    }
}
