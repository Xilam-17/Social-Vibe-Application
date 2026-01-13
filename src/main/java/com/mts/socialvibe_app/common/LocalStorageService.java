package com.mts.socialvibe_app.common;

import jdk.jfr.ContentType;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
        File directory = new File(uploadPath);
        if(!directory.exists()) {
            directory.mkdirs();
        }

        log.info(file.getContentType());
        Path path = null;
        String uri = null;
        String fileName = UUID.randomUUID()+"_"+file.getOriginalFilename();
        if (imageTypes.contains(file.getContentType())){

             path = Paths.get(uploadPath+"images/",fileName);
             uri = "images/"+fileName;
        } else if (docsTypes.contains(file.getContentType())) {
            path = Paths.get(uploadPath+"docs/", fileName);
            uri = "docs/"+fileName;
        } else {
            throw new RuntimeException("Unsupported file");

        }
        //check docs type


        if(path != null) {
            Files.write(path, file.getBytes());
        }



       /* String fileName = UUID.randomUUID()+"_"+file.getOriginalFilename();
        Path path = Paths.get(uploadPath, fileName);

        Files.write(path, file.getBytes());*/

/*
        return "/images/" + fileName;
*/
        return uri;
    }

    public void deleteFile(String file) {
        try {
            String cleanName = file.replace("/images/", "");
            Files.deleteIfExists(Paths.get(uploadPath, cleanName));
        } catch (IOException ex) {
            System.err.println("Could not delete local file : " + ex.getMessage());
        }
    }
 }
