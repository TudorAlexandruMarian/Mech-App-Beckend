package com.example.Mech_App.repositories;

import com.example.Mech_App.bo.DefaultMaintenanceItem;
import com.example.Mech_App.bo.Document;
import com.example.Mech_App.configs.CustomResponseStatusException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    default Document findByIdRequired(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Document not found with id: " + id));
    }

}
