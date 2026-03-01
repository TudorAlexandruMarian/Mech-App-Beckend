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
    String email;
    String password;

}
