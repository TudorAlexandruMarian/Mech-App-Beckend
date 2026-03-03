package com.example.Mech_App.services.impl;

import com.example.Mech_App.bo.Car;
import com.example.Mech_App.bo.DefaultMaintenanceItem;
import com.example.Mech_App.models.CarLastChangesForRemainder;
import com.example.Mech_App.repositories.ReminderRepository;
import com.example.Mech_App.services.DefaultMaintenanceItemService;
import com.example.Mech_App.services.ReminderService;
import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final ServiceFactory serviceFactory;

    private void createReminders() {
        List<DefaultMaintenanceItem> allItemsParam = serviceFactory.getDefaultMaintenanceItemService().getAll();
        List<Car> allCarsList =serviceFactory.getCarService().getAll();

        for(Car car : allCarsList){

            CarLastChangesForRemainder elementsToCheck = serviceFactory.getCarMaintenanceEntryService().findLatestChangesByCarForReminder(car.getId());

       //TODO here

        }


    }

}
