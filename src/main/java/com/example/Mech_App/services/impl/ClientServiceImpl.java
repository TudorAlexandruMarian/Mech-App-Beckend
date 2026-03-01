package com.example.Mech_App.services.impl;

import com.example.Mech_App.bo.Client;
import com.example.Mech_App.bo.User;
import com.example.Mech_App.configs.CustomResponseStatusException;
import com.example.Mech_App.enums.UserRole;
import com.example.Mech_App.models.ClientFilters;
import com.example.Mech_App.repositories.ClientRepository;
import com.example.Mech_App.repositories.UserRepository;
import com.example.Mech_App.services.ClientService;
import com.example.Mech_App.specifications.ClientSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Override
    public void createClient(Client client) {
        client.setId(null);
        client.setPassword(generatePassword());
        String phone = client.getPhoneNumber() != null ? client.getPhoneNumber().trim() : null;
        if (phone != null && !phone.isBlank()) {
            clientRepository.findByPhoneNumber(phone)
                    .ifPresent(existing -> {
                        throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number already in use.");
                    });
            client.setPhoneNumber(phone);
        }
        clientRepository.save(client);
        // Create User with CLIENT role linked to this client (no password for phone-only auth)
        User user = new User();
        user.setPhoneNumber(client.getPhoneNumber());
        user.setRole(UserRole.CLIENT);
        user.setClientId(client.getId());
        user.setPasswordHash(null);  // CLIENT signs in with phone only
        userRepository.save(user);
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
        String newPhone = newClientData.getPhoneNumber() != null ? newClientData.getPhoneNumber().trim() : null;
        String currentPhone = existingClient.getPhoneNumber() != null ? existingClient.getPhoneNumber().trim() : null;
        if (newPhone != null && !newPhone.isBlank() && !newPhone.equals(currentPhone)) {
            clientRepository.findByPhoneNumber(newPhone)
                    .ifPresent(other -> {
                        if (!other.getId().equals(id)) {
                            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number already in use.");
                        }
                    });
        }
        existingClient.setEmail(newClientData.getEmail());
        existingClient.setName(newClientData.getName());
        existingClient.setPhoneNumber(newPhone != null ? newPhone : newClientData.getPhoneNumber());
        // Sync User phone if linked
        userRepository.findByClientId(id).ifPresent(user -> {
            user.setPhoneNumber(existingClient.getPhoneNumber());
            userRepository.save(user);
        });
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
        userRepository.findByClientId(id).ifPresent(userRepository::delete);
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
