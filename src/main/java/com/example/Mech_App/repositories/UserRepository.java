package com.example.Mech_App.repositories;

import com.example.Mech_App.bo.User;
import com.example.Mech_App.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndRole(String phoneNumber, UserRole role);

    Optional<User> findByClientId(Long clientId);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
