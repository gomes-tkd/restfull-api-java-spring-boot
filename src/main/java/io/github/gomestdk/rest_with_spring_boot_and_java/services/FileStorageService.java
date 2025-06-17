package io.github.gomestdk.rest_with_spring_boot_and_java.services;

import io.github.gomestdk.rest_with_spring_boot_and_java.config.FileStorageConfig;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.FileNotFoundException;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {

        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath()
                .toAbsolutePath().normalize();

        try {
            logger.info("Creating upload directory at: {}", this.fileStorageLocation);
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            logger.error("Failed to create upload directory: {}", this.fileStorageLocation, e);
            throw new FileStorageException("Could not create the directory where files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                logger.error("Invalid file path sequence detected: {}", fileName);
                throw new FileStorageException("Sorry! Filename Contains a Invalid path Sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            logger.info("File successfully stored: {}", targetLocation);

            return fileName;
        } catch (IOException ex) {
            logger.error("Could not store file {}. Error: {}", fileName, ex.getMessage(), ex);
            throw new FileStorageException("Could not store file " + fileName + ". Please try Again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                logger.info("File successfully loaded as Resource: {}", filePath);
                return resource;
            } else {
                logger.error("File not found or not readable: {}", fileName);
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (Exception ex) {
            logger.error("Error while loading file {} as Resource. Error: {}", fileName, ex.getMessage(), ex);
            throw new FileNotFoundException("File not found: " + fileName, ex);
        }
    }
}
