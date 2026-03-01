package com.example.Mech_App.bo;

import com.example.Mech_App.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String email;

    @Column(unique = true)
    String phoneNumber;

    /**
     * Required for ADMIN. For CLIENT (phone-only auth) can be null.
     */
    String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserRole role;

    /**
     * For CLIENT role: links to Client.id
     */
    Long clientId;
}
