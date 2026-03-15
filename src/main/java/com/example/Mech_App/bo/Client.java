package com.example.Mech_App.bo;

import com.example.Mech_App.utils.Audit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Client extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @Column(unique = true)
    String phoneNumber;
    /** Auto-generated in AAAA1111 format (4 letters A–Z, 4 digits 0–9). */
    @Column(unique = true)
    String identifier;
    String email;
    String password;

}
