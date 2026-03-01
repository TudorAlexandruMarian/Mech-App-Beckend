package com.example.Mech_App.controllers;

import com.example.Mech_App.bo.Document;
import com.example.Mech_App.enums.DocumentType;
import com.example.Mech_App.repositories.DocumentRepository;
import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {
    private final ServiceFactory serviceFactory;
    private final DocumentRepository documentRepository;

    @GetMapping("/list")
    public ResponseEntity<List<Document>> listByServiceEntryAndType(
            @RequestParam Long serviceEntryId,
            @RequestParam DocumentType documentType
    ) {
        List<Document> docs = serviceFactory.getServiceEntryDocumentService()
                .findByServiceEntryAndDocType(serviceEntryId, documentType);
        return ResponseEntity.ok(docs);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Document> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam DocumentType section,
            @RequestParam Long serviceEntryId
    ) {
        Document doc = serviceFactory.getServiceEntryDocumentService()
                .createDocument(file, section, serviceEntryId);
        return ResponseEntity.ok(doc);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        serviceFactory.getServiceEntryDocumentService().deleteDocument(id);
        return ResponseEntity.ok("Document deleted");
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Map<String, String>> getFileBase64(@PathVariable Long id) {
        Document doc = documentRepository.findByIdRequired(id);
        String base64 = serviceFactory.getDocumentService().getFileBase64(
                doc.getDocPath(),
                doc.getDocumentType(),
                doc.getServiceEntryId()
        );
        return ResponseEntity.ok(Map.of("base64", base64, "docPath", doc.getDocPath()));
    }
}
