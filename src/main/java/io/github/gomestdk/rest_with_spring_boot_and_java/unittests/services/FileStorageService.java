package io.github.gomestdk.rest_with_spring_boot_and_java.unittests.services;

import io.github.gomestdk.rest_with_spring_boot_and_java.config.FileStorageConfig;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
    public FileStorageService(FileStorageConfig fileStorageLocation) {
        //  Path path = Paths.get(fileStorageLocation.getUploadDir());
        this.fileStorageLocation = Paths.get(fileStorageLocation.getUploadDir())
                .toAbsolutePath().toAbsolutePath()
                .normalize();

        try {
            logger.info("Creating Directories");
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            logger.error("Could not create the directory where files will be stored!");
            throw new FileStorageException("Could not create the directory where files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                logger.error("Sorry! Filename contains a invalid path sequence: {}", fileName);

                throw new FileStorageException(("Sorry! Filename contains a invalid path sequence: " + fileName));
            }

            logger.info("Saving file in disk");

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            logger.error("Could not store file: {}. Please try again.", fileName);

            throw new FileStorageException(("Could not store file: " + fileName + ". Please try again."), e);
        }
    }
}
