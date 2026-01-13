/*
package com.mts.socialvibe_app.common; // adjust to your package structure

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        // 1. Generate a unique name for the file
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 2. Build the Supabase Storage URL
        // URL format: {PROJECT_URL}/storage/v1/object/{BUCKET_NAME}/{FILE_NAME}
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + fileName;

        RestTemplate restTemplate = new RestTemplate();

        // 3. Setup Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(supabaseKey); // The 'anon' key acts as the token
        headers.setContentType(MediaType.valueOf(file.getContentType()));

        // 4. Create the Request
        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);

        // 5. Execute the POST request to Supabase
        restTemplate.postForEntity(uploadUrl, entity, String.class);

        // 6. Return the Public URL for your database
        // Format: {PROJECT_URL}/storage/v1/object/public/{BUCKET_NAME}/{FILE_NAME}
        return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + fileName;
    }

    public void deleteFile(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) return;

        // 1. Extract the file name from the URL
        // URL: .../storage/v1/object/public/posts/filename.jpg
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        // 2. Build the Delete URL
        String deleteUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + fileName;

        RestTemplate restTemplate = new RestTemplate();

        // 3. Setup Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(supabaseKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 4. Execute the DELETE request
        try {
            restTemplate.exchange(deleteUrl, org.springframework.http.HttpMethod.DELETE, entity, String.class);
        } catch (Exception e) {
            // We log the error but don't stop the process (the DB record is already being deleted)
            System.err.println("Failed to delete file from Supabase: " + e.getMessage());
        }
    }
}*/
