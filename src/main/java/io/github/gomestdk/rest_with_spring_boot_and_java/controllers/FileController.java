package io.github.gomestdk.rest_with_spring_boot_and_java.controllers;

import io.github.gomestdk.rest_with_spring_boot_and_java.controllers.docs.FileControllerDocs;
import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.UploadFileResponseDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.unittests.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/file/v1")
@Tag(name = "File Endpoint", description = "Operações para upload e download de arquivos")
public class FileController implements FileControllerDocs {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    @Override
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/file/v1/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponseDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @Override
    public List<UploadFileResponseDTO> uploadMultipleFile(MultipartFile[] files) {
        return List.of();
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String file, HttpServletRequest request) {
        return null;
    }
}
