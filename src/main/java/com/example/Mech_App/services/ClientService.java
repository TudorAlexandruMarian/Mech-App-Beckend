package com.example.Mech_App.services;

import com.example.Mech_App.bo.Client;
import com.example.Mech_App.models.ClientFilters;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    void createClient(Client client);

    Client getClient(Long id);

    void editClient(Long id, Client newClientData);

    @Transactional
    String resetPassword(Long id);

    void deleteClient(Long id);

    Page<Client> getAllClients(ClientFilters filters, Pageable pageable);

}
