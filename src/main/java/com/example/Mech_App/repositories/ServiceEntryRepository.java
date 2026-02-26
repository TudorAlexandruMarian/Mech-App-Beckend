package com.example.Mech_App.repositories;

import com.example.Mech_App.bo.ServiceEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceEntryRepository extends JpaRepository<ServiceEntry, Long> {
}
