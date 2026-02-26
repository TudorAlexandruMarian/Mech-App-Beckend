package com.example.Mech_App.services;

import com.example.Mech_App.enums.DocumentType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    String storeFile(MultipartFile file, DocumentType section, Long parentId);

    String getFileBase64(String filename, DocumentType section, Long parentId);

    Resource getExcel(String filename, DocumentType section, Long parentId);

    void deleteFile(String fileName, DocumentType section, Long parentId);
}
