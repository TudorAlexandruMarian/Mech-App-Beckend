package com.example.Mech_App.configs;

import com.example.Mech_App.bo.Client;
import com.example.Mech_App.bo.User;
import com.example.Mech_App.enums.UserRole;
import com.example.Mech_App.repositories.ClientRepository;
import com.example.Mech_App.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "admin@mech.local";
    private static final String ADMIN_PASSWORD = "Admin123!";

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      ClientRepository clientRepository,
                      org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
            User admin = new User();
            admin.setEmail(ADMIN_EMAIL);
            admin.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setRole(UserRole.ADMIN);
            admin.setPhoneNumber(null);
            admin.setClientId(null);
            userRepository.save(admin);
        }
        // Migrate existing clients: create User for each Client that has no User; use "phone-identifier" if client has identifier
        List<Client> clients = clientRepository.findAll();
        for (Client c : clients) {
            if (c.getPhoneNumber() != null && !c.getPhoneNumber().isBlank()
                    && userRepository.findByClientId(c.getId()).isEmpty()) {
                User u = new User();
                String stored = c.getIdentifier() != null && !c.getIdentifier().isBlank()
                        ? c.getPhoneNumber() + "-" + c.getIdentifier()
                        : c.getPhoneNumber();
                u.setPhoneNumber(stored);
                u.setRole(UserRole.CLIENT);
                u.setClientId(c.getId());
                u.setPasswordHash(null);
                userRepository.save(u);
            }
        }
    }
}
