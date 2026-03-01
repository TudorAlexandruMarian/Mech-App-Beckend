package com.example.Mech_App.services;

import com.example.Mech_App.bo.Document;
import com.example.Mech_App.enums.DocumentType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ServiceEntryDocumentService {
    Document createDocument(MultipartFile file, DocumentType section, Long serviceEntryId);

    @Modifying
    void deleteDocument(Long docId);

    List<Document> findByServiceEntryAndDocType(Long serviceEntryId, DocumentType docType);
}
