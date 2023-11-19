package com.lotarys.devlink.services;

import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.exceptions.GetPhotoException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class PhotoService {
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
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return entity;
    }

    private String createPath(String username) {
        return username + "_photo";
    }

    private void deleteFile(User user) {
        HttpHeaders headers = createEntityWithOauth().getHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(user.getPhoto() + "&permanently=true",
                HttpMethod.DELETE,
                entity,
                String.class);
    }

    private void uploadFile(MultipartFile file, String uploadUrl) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> entityForPostFile = new HttpEntity<>(body, null);
        ResponseEntity<String> response = restTemplate.exchange(uploadUrl,
                HttpMethod.PUT,
                entityForPostFile,
                String.class);
    }

    private String getUrlForDownload(String username) {
        ResponseEntity<String> getUrl = restTemplate.exchange(
                "https://cloud-api.yandex.net/v1/disk/resources/upload?path=" + createPath(username),
                HttpMethod.GET,
                createEntityWithOauth(),
                String.class);
        JsonObject jsonObject = Json.createReader(new StringReader(getUrl.getBody()))
                .readObject();
        return jsonObject.getString("href");
    }

    public InputStreamResource getPhoto(String username) {
        try {
            InputStream fileStream = new URL(getUrlForDownload(username)).openStream();
            return new InputStreamResource(fileStream);
        } catch (Exception e) {
            throw new GetPhotoException("Failed to get photo");
        }
    }

    public String postFile(User user, MultipartFile file) {
        String username = user.getUsername();
        deleteFile(user);
        uploadFile(file,getUrlForUpload(username));
        return "https://cloud-api.yandex.net/v1/disk/resources?path" + createPath(username);
    }
}
