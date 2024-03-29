package com.lotarys.devlink.services;

import com.lotarys.devlink.entities.Card;
import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.exceptions.GetPhotoException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import javax.json.Json;
import javax.json.JsonObject;
import java.io.*;
import java.net.URL;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${yandex.token}")
    private String OAUTH_TOKEN;

    private RestTemplate restTemplate = new RestTemplate();

    private String getUrlForUpload(String username) {
        ResponseEntity<String> getUrl = restTemplate.exchange(
                "https://cloud-api.yandex.net/v1/disk/resources/upload?path=" + createPath(username),
                HttpMethod.GET, createEntityWithOauth(),
                String.class);
        JsonObject jsonObject = Json.createReader(new StringReader(getUrl.getBody()))
                .readObject();
        return jsonObject.getString("href");
    }

    private HttpEntity createEntityWithOauth() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "OAuth " + OAUTH_TOKEN);
        return new HttpEntity<>("parameters", headers);
    }

    private String createPath(String username) {
        return username + "_photo";
    }

    private void deleteImage(String image) {
        HttpHeaders headers = createEntityWithOauth().getHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange("https://cloud-api.yandex.net/v1/disk/resources?path=" + image + "_photo" + "&permanently=true",
                HttpMethod.DELETE,
                entity,
                String.class);
    }

    private void uploadImage(MultipartFile file, String uploadUrl) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> entityForPostFile = new HttpEntity<>(body, null);
        restTemplate.exchange(uploadUrl,
                HttpMethod.PUT,
                entityForPostFile,
                String.class);
    }

    private String getUrlForDownload(String username) {
        ResponseEntity<String> getUrl = restTemplate.exchange(
                "https://cloud-api.yandex.net/v1/disk/resources/download?path=" + createPath(username),
                HttpMethod.GET,
                createEntityWithOauth(),
                String.class);
        JsonObject jsonObject = Json.createReader(new StringReader(getUrl.getBody()))
                .readObject();
        return jsonObject.getString("href");
    }

    private String getImageInBase64(String username) {
        try {
            InputStream fileStream = new URL(getUrlForDownload(username)).openStream();
            byte[] imageBytes = IOUtils.toByteArray(fileStream);
            String encodedString = Base64.getEncoder().encodeToString(imageBytes);
            return encodedString;
        } catch (Exception e) {
            throw new GetPhotoException("Failed to get photo");
        }
    }

    public String getUserImage(User user) {
            if(user.getPhoto().equals("default"))
            {
               return getImageInBase64(user.getPhoto());
            } else {
               return getImageInBase64(user.getUsername());
            }
    }

    public String getCardImage(Card card) {
        try {
            return getImageInBase64(card.getUrl());
        } catch (GetPhotoException e) {
            return getImageInBase64(card.getUser().getUsername());
        }
    }

    public void deleteCardImage(String url) {
        try {
            deleteImage(url);
        } catch (Exception e) {
        }
    }

    public void postCardImage(MultipartFile file, String url) {
            try {
                deleteImage(url);
            } catch (Exception e) {

            }
            uploadImage(file, getUrlForUpload(url));
    }

    public String postUserImage(User user, MultipartFile file) {
        String username = user.getUsername();
        if(!user.getPhoto().equals("default")) {
            deleteImage(username);
        }
        uploadImage(file, getUrlForUpload(username));
        return "https://cloud-api.yandex.net/v1/disk/resources?path=" + createPath(username);
    }
}
