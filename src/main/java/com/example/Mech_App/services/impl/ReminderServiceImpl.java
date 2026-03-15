package com.example.Mech_App.services.impl;

import com.example.Mech_App.bo.*;
import com.example.Mech_App.models.CarLastChangesForRemainder;
import com.example.Mech_App.repositories.CarMaintenanceEntryRepository;
import com.example.Mech_App.repositories.ReminderRepository;
import com.example.Mech_App.services.ReminderService;
import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final CarMaintenanceEntryRepository carMaintenanceEntryRepository;
    private final ServiceFactory serviceFactory;

    @Override
    public void createReminders() {
        List<Car> allCarsList = serviceFactory.getCarService().getAll();
        for (Car car : allCarsList) {
            try {
                CarLastChangesForRemainder elementsToCheck = serviceFactory.getCarMaintenanceEntryService().findLatestChangesByCarForReminder(car.getId());
                checkCarItemsAndNotify(elementsToCheck, car);
            } catch (Exception e) {
                System.out.println("Fail to check car " + car.getId());
            }
        }
    }


    public void checkCarItemsAndNotify(CarLastChangesForRemainder elementToCheck, Car car) {
        Long lastCarOdometer = elementToCheck.getLastServiceEntry().getOdometer();

        for (CarLastChangesForRemainder.ItemToCheck itemCheck : elementToCheck.getItemsToCheck()) {
            try {
                if (!Boolean.TRUE.equals(itemCheck.getCarMaintenanceEntry().getRemainderSent())) {
                    continue;
                }

                ServiceEntry itemServiceEntry = serviceFactory.getServiceEntryService().getServiceEntry(itemCheck.getCarMaintenanceEntry().getServiceEntryId());
                Long itemOdometer = itemServiceEntry.getOdometer();
                LocalDate itemServiceEntryDate = itemServiceEntry.getFinishDate().toLocalDate();

                Boolean needsToBeReplaceByTime = false;
                Boolean needsToBeReplaceByKm = false;

                Boolean timeValidationCanBePerformed = itemCheck.getDefaultMaintenanceItem().getMaxLifeTime() != null;
                Boolean kmValidationCanBePerformed = itemCheck.getDefaultMaintenanceItem().getMaxLifeKm() != null;

                if (timeValidationCanBePerformed) {
                    if (LocalDate.now().isAfter(itemServiceEntry.getFinishDate().toLocalDate().plusDays(itemCheck.getDefaultMaintenanceItem().getMaxLifeTime()))) {
                        needsToBeReplaceByTime = true;
                    }
                }

                if (kmValidationCanBePerformed) {
                    if (lastCarOdometer > (itemOdometer + itemCheck.getDefaultMaintenanceItem().getMaxLifeKm())) {
                        needsToBeReplaceByKm = true;
                    }
                }

                if (needsToBeReplaceByKm) {
                    String message = "Masaina cu numarul de inmatruclulare " + car.getLicenseNo() + " a depasit durata de viata a elenetului de mentenata " + itemCheck.getDefaultMaintenanceItem().getName() + ". Acesta avand o durata maxima de viata de " + itemCheck.getDefaultMaintenanceItem().getMaxLifeKm() + " km. Ultima schimabre a fost la " + itemOdometer + ", masina avand acum " + lastCarOdometer;
                    reminderRepository.save(Remainder.builder()
                            .carId(car.getId())
                            .carMaintenanceEntry(itemCheck.getCarMaintenanceEntry().getId())
                            .clientId(car.getClientId())
                            .sawByAdmin(false)
                            .sawByClient(false)
                            .message(message)
                            .build());

                    CarMaintenanceEntry entry = itemCheck.getCarMaintenanceEntry();
                    entry.setRemainderSent(true);
                    carMaintenanceEntryRepository.save(entry);

                    continue;
                }

                if (needsToBeReplaceByTime) {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String formattedDate = itemServiceEntryDate.format(formatter);

                    String message = "Masaina cu numarul de inmatruclulare " + car.getLicenseNo() + " a depasit durata de viata a elenetului de mentenata " + itemCheck.getDefaultMaintenanceItem().getName() + ". Acesta avand o durata maxima de viata de " + itemCheck.getDefaultMaintenanceItem().getMaxLifeTime() + " zile, iar ultima schimabre a fost pe data de " + formattedDate;
                    reminderRepository.save(Remainder.builder()
                            .carId(car.getId())
                            .carMaintenanceEntry(itemCheck.getCarMaintenanceEntry().getId())
                            .clientId(car.getClientId())
                            .sawByAdmin(false)
                            .sawByClient(false)
                            .message(message)
                            .build());

                    CarMaintenanceEntry entry = itemCheck.getCarMaintenanceEntry();
                    entry.setRemainderSent(true);
                }
            } catch (Exception e) {
                System.out.println("Fail to check car " + +car.getId() + " for ment item " + itemCheck.getCarMaintenanceEntry().getId());
            }

        }

    }

    @Override
    public Page<Remainder> findAllRemindersByClientId(Long clientId, Pageable pageable) {
        return reminderRepository.findAllByClientIdOrderByIdDesc(clientId, pageable);
    }
    @Override
    public Page<Remainder> findAllRemindersForAdmin(Pageable pageable) {
        return reminderRepository.findAllByOrderByIdDesc(pageable);
    }
    @Override
    public Page<Remainder> findAllRemindersForAdminNew(Pageable pageable) {
        return reminderRepository.findAllBySawByAdminOrderByIdDesc(false, pageable);
    }
    @Override
    public void updateReminderStatusByAdmin(Long reminderId){
        Remainder existingReminder= reminderRepository.findByIdRequired(reminderId);
        existingReminder.setSawByAdmin(true);
        reminderRepository.save(existingReminder);
    }

    @Override
    public void updateReminderStatusByClient(Long reminderId){
        Remainder existingReminder= reminderRepository.findByIdRequired(reminderId);
        existingReminder.setSawByClient(true);
        reminderRepository.save(existingReminder);
    }
}
