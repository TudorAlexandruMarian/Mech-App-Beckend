package com.example.Mech_App.services.impl;

import com.example.Mech_App.configs.CustomResponseStatusException;
import com.example.Mech_App.enums.DocumentType;
import com.example.Mech_App.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final FileSystemStorageService fileSystemStorageService;
    private static final Tika tika = new Tika();

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/png", "image/jpeg"
    );
    private static final Set<String> ALLOWED_MEDIA_TYPES = Set.of(
            "application/pdf", "image/png", "image/jpeg"
    );

    @Override
    public String storeFile(MultipartFile file, DocumentType section, Long parentId) {

        if (file == null || file.isEmpty()) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file is empty or null.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file has no name.");
        }

        String cleanedFilename = StringUtils.cleanPath(originalFilename);
        if (cleanedFilename.contains("..")) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid file name: potential path traversal attempt.");
        }

        validateFileContent(file, section);

        return fileSystemStorageService.storeWithSection(file, section, parentId);
    }

    private void validateFileContent(MultipartFile file, DocumentType section) {
        Set<String> allowedTypes = switch (section) {
            case POZA-> ALLOWED_IMAGE_TYPES;
            case FACTURA -> ALLOWED_MEDIA_TYPES;
            default -> null;
        };

        if (allowedTypes == null) {
            return;
        }

        try {
            String detectedType = tika.detect(file.getInputStream());
            if (!allowedTypes.contains(detectedType)) {
                throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid file content. Detected type: " + detectedType + ". Allowed types: " + allowedTypes);
            }
        } catch (IOException e) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to read file content for validation.");
        }
    }



    @Override
    public String getFileBase64(String filename, DocumentType section, Long parentId) {
        validateEnumPath(section);
        try {
            MimeBytes mimeBytes = getValidatedFile(filename, section, parentId);
            return toDataUrl(mimeBytes.bytes, mimeBytes.mimeType);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public Resource getExcel(String filename, DocumentType section, Long parentId) {
        return fileSystemStorageService.loadAsResource(filename, section, parentId);
    }

    private MimeBytes getValidatedFile(String filename, DocumentType section, Long parentId) {
        try {
            if (filename != null) {
                filename = sanitizeFilename(filename);

                InputStream inputStream = fileSystemStorageService
                        .loadAsResource(filename, section, parentId)
                        .getInputStream();

                byte[] bytes = IOUtils.toByteArray(inputStream);
                String mimeType = getMimeType(filename);

                if (!isSafeMimeType(mimeType)) {
                    throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file type: " + mimeType);
                }

                return new MimeBytes(bytes, mimeType);
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "File not found: " + e.getMessage());
        } catch (Exception e) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to read file");
        }
    }

    private String sanitizeFilename(String filename) {
        filename = filename.replaceAll("[\\r\\n\\t\\x00]", "");
        return filename.replaceAll("[^a-zA-Z0-9._-]", "");
    }


    private String toDataUrl(byte[] fileBytes, String mimeType) {
        String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
        return "data:" + mimeType + ";base64," + base64Encoded;
    }

    private record MimeBytes(byte[] bytes, String mimeType) {
    }

    public String copyStoredFile(String originalFilename, DocumentType sourceSection, Long sourceParentId, DocumentType targetSection, Long targetParentId) {
        return fileSystemStorageService.copyFileBetweenSections(originalFilename, sourceSection, sourceParentId, targetSection, targetParentId);
    }

    @Override
    public void deleteFile(String fileName, DocumentType section, Long parentId) {
        fileSystemStorageService.deleteFile(fileName, section, parentId);
    }


    private boolean isSafeMimeType(String mimeType) {
        return mimeType != null && (
                ALLOWED_MEDIA_TYPES.contains(mimeType) ||
                        mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        );
    }


    public static String toDataUrl(byte[] fileBytes) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(fileBytes);
        String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
        return "data:" + mimeType + ";base64," + base64Encoded;
    }


    public static String getMimeType(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "application/octet-stream"; // default fallback
        }

        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

        return switch (ext) {
            case "mp4" -> "video/mp4";
            case "mov" -> "video/quicktime";
            case "avi" -> "video/x-msvideo";
            case "mkv" -> "video/x-matroska";

            case "mp3" -> "audio/mpeg";
            case "wav" -> "audio/wav";
            case "aac" -> "audio/aac";

            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";

            case "pdf" -> "application/pdf";
            case "txt" -> "text/plain";
            case "html", "htm" -> "text/html";
            case "json" -> "application/json";

            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "xls"  -> "application/vnd.ms-excel";

            default -> "application/octet-stream";
        };
    }

    public static String detectMimeType(byte[] fileBytes) {
        Tika tika = new Tika();
        return tika.detect(fileBytes);
    }


    public static void validateEnumPath(DocumentType section) {
        if (section == null) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "FileStorageSection cannot be null.");
        }
        for (DocumentType s : DocumentType.values()) {
            if (s == section) {
                return;
            }
        }
        throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid FileStorageSection: " + section);
    }
}
