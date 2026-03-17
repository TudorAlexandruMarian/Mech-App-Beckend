package com.example.Mech_App.services.impl;

import com.example.Mech_App.bo.Client;
import com.example.Mech_App.bo.User;
import com.example.Mech_App.configs.CustomResponseStatusException;
import com.example.Mech_App.enums.UserRole;
import com.example.Mech_App.models.ClientFilters;
import com.example.Mech_App.repositories.ClientRepository;
import com.example.Mech_App.repositories.UserRepository;
import com.example.Mech_App.services.CarService;
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
    /** A–Z excluding I and O to avoid confusion with 1 and 0. */
    private static final String LETTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final int PASSWORD_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final CarService carService;

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
        client.setIdentifier(generateUniqueIdentifier());
        clientRepository.save(client);
        // Create User with CLIENT role: phoneNumber stored as "phone-identifier"
        User user = new User();
        user.setPhoneNumber(toStoredPhoneNumber(client.getPhoneNumber(), client.getIdentifier()));
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
        boolean phoneChanged = newPhone != null && !newPhone.isBlank() && !newPhone.equals(currentPhone);
        if (phoneChanged) {
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
        if (phoneChanged) {
            existingClient.setIdentifier(generateUniqueIdentifier());
        }
        // Sync app_user: store phone-identifier
        userRepository.findByClientId(id).ifPresent(user -> {
            user.setPhoneNumber(toStoredPhoneNumber(existingClient.getPhoneNumber(), existingClient.getIdentifier()));
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
        Client client = clientRepository.findByIdRequired(id);

        // Delete all cars (and their related data) owned by this client
        carService.getAllCarsByCustomer(client.getId())
                .forEach(car -> carService.deleteCar(car.getId()));

        // Delete linked app_user
        userRepository.findByClientId(id).ifPresent(userRepository::delete);

        // Finally delete the client itself
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
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    /** AAAA1111 format: 4 letters A–Z, 4 digits 0–9. */
    private String generateIdentifier() {
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 4; i++) {
            sb.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        for (int i = 0; i < 4; i++) {
            sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return sb.toString();
    }

    private String generateUniqueIdentifier() {
        for (int attempt = 0; attempt < 100; attempt++) {
            String id = generateIdentifier();
            if (clientRepository.findByIdentifier(id).isEmpty()) {
                return id;
            }
        }
        throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not generate unique identifier.");
    }

    private static String toStoredPhoneNumber(String phone, String identifier) {
        if (phone == null || phone.isBlank()) return null;
        if (identifier == null || identifier.isBlank()) return phone;
        return phone + "-" + identifier;
    }
}
