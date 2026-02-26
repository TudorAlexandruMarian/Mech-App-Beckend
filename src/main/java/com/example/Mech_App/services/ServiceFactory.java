package com.example.Mech_App.services;

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
}
