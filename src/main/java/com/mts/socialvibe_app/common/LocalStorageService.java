package com.mts.socialvibe_app.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LocalStorageService {

    @Value("${file.upload-dir}")
    private String uploadPath;

    public static final List<String> imageTypes = List.of(MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE);
    public static final List<String> docsTypes = List.of(MediaType.APPLICATION_PDF_VALUE, MediaType.TEXT_PLAIN_VALUE, MediaType.TEXT_MARKDOWN_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE, "application/msword");

    public String saveFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        log.info("Uploading file type: {}", contentType);

        String subFolder;
        String uriPrefix;

        if (imageTypes.contains(contentType)) {
            subFolder = "images/";
            uriPrefix = "/images/";
        } else if (docsTypes.contains(contentType)) {
            subFolder = "docs/";
            uriPrefix = "/docs/";
        } else {
            throw new RuntimeException("Unsupported file type: " + contentType);
        }

        Path directoryPath = Paths.get(uploadPath).resolve(subFolder);

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = directoryPath.resolve(fileName);

        Files.write(filePath, file.getBytes());

        return uriPrefix + fileName;
    }

    public void deleteFile(String fileUrl) {
        try {
            String relativePath = fileUrl.startsWith("/") ? fileUrl.substring(1) : fileUrl;
            Path pathToDelete = Paths.get(uploadPath).resolve(relativePath);

            Files.deleteIfExists(pathToDelete);

        } catch (IOException ex) {
            log.error("Could not delete local file: {}", ex.getMessage());
        }
    }
}
