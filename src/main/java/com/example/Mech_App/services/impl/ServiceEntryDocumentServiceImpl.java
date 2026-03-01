package com.example.Mech_App.services.impl;

import com.example.Mech_App.bo.Document;
import com.example.Mech_App.enums.DocumentType;
import com.example.Mech_App.repositories.DocumentRepository;
import com.example.Mech_App.services.ServiceEntryDocumentService;
import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceEntryDocumentServiceImpl implements ServiceEntryDocumentService {
    private final ServiceFactory serviceFactory;
    private final DocumentRepository documentRepository;


    @Override
    public void createDocument(MultipartFile file, DocumentType section, Long serviceEntryId) {
        Document newDocument = Document.builder().documentType(section).serviceEntryId(serviceEntryId).build();
        newDocument.setDocPath(serviceFactory.getDocumentService().storeFile(file, section, serviceEntryId));
        documentRepository.save(newDocument);
    }


    @Modifying
    @Override
    public void deleteDocument(Long docId) {
        Document existimgDocument = documentRepository.findByIdRequired(docId);
        serviceFactory.getDocumentService().deleteFile(existimgDocument.getDocPath(), existimgDocument.getDocumentType(), existimgDocument.getServiceEntryId());
        documentRepository.deleteById(docId);
    }


    @Override
    public List<Document> findByServiceEntryAndDocType(Long serviceEntryId, DocumentType docType) {
        return documentRepository.findByServiceEntryIdAndDocumentType(serviceEntryId, docType);
    }

    ;


}
