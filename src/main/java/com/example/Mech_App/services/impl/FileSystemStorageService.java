package com.example.Mech_App.services.impl;

import com.example.Mech_App.configs.CustomResponseStatusException;
import com.example.Mech_App.enums.DocumentType;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

@Service
@ConfigurationProperties(prefix = "storage")
public class FileSystemStorageService {
    private String location;
    private Path rootLocation;
    private Date today = new Date();

    @PostConstruct
    public void init() {
        try {
            rootLocation = Paths.get(location);
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Could not initialize storage location:" + e);
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String storeWithSection(MultipartFile file, DocumentType section, Long parentId) {
        try {
            rootLocation = Paths.get(location + File.separator + section + File.separator + parentId)
                    .toAbsolutePath().normalize();
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Could not initialize storage location:" + e);
        }

        String filename = StringUtils.cleanPath(
                getRandomFilename() + "." + FilenameUtils.getExtension(file.getOriginalFilename()));

        try {
            if (file.isEmpty()) {
                throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to store empty file " + filename);
            }

            if (filename.contains("..")) {
                throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Cannot store file with relative path outside current directory " + filename);
            }

            Path targetLocation = rootLocation.resolve(filename).normalize();


            if (!targetLocation.startsWith(rootLocation)) {
                throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid file path: outside of storage directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to store file " + filename + " Error: " + e);
        }

        return filename;
    }



    public Resource loadAsResource(String filename, DocumentType section, Long parentId) {
        try {
            rootLocation = Paths.get(location + File.separator + section + File.separator + parentId);
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }


    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public String getRandomFilename() {
        today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS");
        return sdf.format(today);
    }

    public String copyFileBetweenSections(String filename, DocumentType sourceSection, Long sourceParentId,
                                          DocumentType targetSection, Long targetParentId) {
        try {
            Path sourceDir = Paths.get(location, sourceSection.toString(), sourceParentId.toString());
            Path sourceFile = sourceDir.resolve(filename);

            if (!Files.exists(sourceFile)) {
                throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, "Source file does not exist: " + filename);
            }

            Path targetDir = Paths.get(location, targetSection.toString(), targetParentId.toString());
            Files.createDirectories(targetDir);

            try (Stream<Path> files = Files.list(targetDir)) {
                files
                        .filter(Files::isRegularFile)
                        .forEach(f -> {
                            try {
                                Files.deleteIfExists(f);
                            } catch (IOException e) {
                                throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete existing file: " + f.getFileName(), e);
                            }
                        });
            }

            String extension = FilenameUtils.getExtension(filename);
            String newFilename = getRandomFilename() + "." + extension;
            Path targetFile = targetDir.resolve(newFilename);

            Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);

            return newFilename;

        } catch (IOException e) {
            throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to copy file", e);
        }
    }


    public void deleteFile(String filename, DocumentType section, Long parentId) {
        try {
            Path fileDir = Paths.get(location, section.toString(), parentId.toString());
            Path filePath = fileDir.resolve(filename);

            if (!Files.exists(filePath)) {
                throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, "File does not exist: " + filename);
            }

            Files.delete(filePath);

            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(fileDir)) {
                if (!dirStream.iterator().hasNext()) {
                    Files.delete(fileDir);
                }
            }

        } catch (IOException e) {
            throw new CustomResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file or directory: " + filename, e);
        }
    }

}
