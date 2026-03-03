package com.example.Mech_App.services;

import com.example.Mech_App.bo.DefaultMaintenanceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServiceFactory {
    private static ApplicationContext context;

    @Autowired
    public ServiceFactory(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public ClientService getClientService() {
        return context.getBean(ClientService.class);
    }

    public CarService getCarService() {
        return context.getBean(CarService.class);
    }

    public ServiceEntryService getServiceEntryService() {
        return context.getBean(ServiceEntryService.class);
    }

    public CarMaintenanceEntryService getCarMaintenanceEntryService() {
        return context.getBean(CarMaintenanceEntryService.class);
    }

    public DefaultMaintenanceItemService getDefaultMaintenanceItemService() {
        return context.getBean(DefaultMaintenanceItemService.class);
    }

    public DocumentService getDocumentService() {
        return context.getBean(DocumentService.class);
    }

    public ReportsService getReportsService() {
        return context.getBean(ReportsService.class);
    }

    public ServiceEntryDocumentService getServiceEntryDocumentService() {
        return context.getBean(ServiceEntryDocumentService.class);
    }

    public ReminderService getReminderService() {
        return context.getBean(ReminderService.class);
    }
}
