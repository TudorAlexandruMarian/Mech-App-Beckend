package com.example.Mech_App.services.impl;

import com.example.Mech_App.bo.Client;
import com.example.Mech_App.models.ClientFilters;
import com.example.Mech_App.repositories.ClientRepository;
import com.example.Mech_App.services.ClientService;
import com.example.Mech_App.specifications.ClientSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    private final ClientRepository clientRepository;

    @Override
    public void createClient(Client client) {
        client.setId(null);
        client.setPassword(generatePassword());
        clientRepository.save(client);
    }

    @Override
    public Client getClient(Long id) {
        Client existingClient = clientRepository.findByIdRequired(id);
        existingClient.setPassword(null);
        return existingClient;
    }

    @Override
    @Transactional
    public void editClient(Long id, Client newClientData) {
        Client existingClient = clientRepository.findByIdRequired(id);
        existingClient.setEmail(newClientData.getEmail());
        existingClient.setName(newClientData.getName());
        existingClient.setPhoneNumber(newClientData.getPhoneNumber());
    }

    @Override
    @Transactional
    public String resetPassword(Long id) {
        Client existingClient = clientRepository.findByIdRequired(id);
        String newPassword = generatePassword();
        existingClient.setPassword(newPassword);
        clientRepository.save(existingClient);
        return newPassword;
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        clientRepository.findByIdRequired(id);
        clientRepository.deleteById(id);
    }

    @Override
    public Page<Client> getAllClients(ClientFilters filters, Pageable pageable) {

        return clientRepository.findAll(
                ClientSpecification.withFilters(filters.getName(), filters.getPhoneNumber()),
                pageable
        );

    }


    public static String generatePassword() {
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }


}
