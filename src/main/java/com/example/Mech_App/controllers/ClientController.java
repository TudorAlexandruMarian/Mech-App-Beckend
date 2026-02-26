package com.example.Mech_App.controllers;

import com.example.Mech_App.bo.Client;
import com.example.Mech_App.models.ClientFilters;
import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ServiceFactory serviceFactory;

    @GetMapping("/get/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(serviceFactory.getClientService().getClient(id));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createClient(@RequestBody Client client) {
        serviceFactory.getClientService().createClient(client);
        return ResponseEntity.ok("Client created successfully!");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editClient(@PathVariable Long id, @RequestBody Client client) {
        serviceFactory.getClientService().editClient(id, client);
        return ResponseEntity.ok("Client updated successfully!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        serviceFactory.getClientService().deleteClient(id);
        return ResponseEntity.ok("Client deleted successfully!");
    }

    @PostMapping("/reset-password/{id}")
    public ResponseEntity<String> resetPassword(@PathVariable Long id) {
        String newPassword = serviceFactory.getClientService().resetPassword(id);
        return ResponseEntity.ok(newPassword);
    }

    @PostMapping("/all")
    public ResponseEntity<Page<Client>> getAllClients(
           @RequestBody ClientFilters filters,
            Pageable pageable
    ) {
        Page<Client> clients = serviceFactory.getClientService().getAllClients(filters, pageable);
        return ResponseEntity.ok(clients);
    }
}