package com.lotarys.devlink.services;

import com.lotarys.devlink.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class PhotoService {
    @Value("${yandex.token}")
    private String OAUTH_TOKEN;

    @Autowired
    private RestTemplate restTemplate;

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

    private void deleteFile(String username) {
        ResponseEntity<String> response = restTemplate.exchange("https://cloud-api.yandex.net/v1/disk/resources?path=" + createPath(username),
                HttpMethod.DELETE,
                createEntityWithOauth(),
                String.class);
    }

    private void uploadFile(String username, MultipartFile file, String uploadUrl) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> entityForPostFile = new HttpEntity<>(body, null);
        ResponseEntity<String> response = restTemplate.exchange(uploadUrl,
                HttpMethod.POST,
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

    public InputStreamResource getPhoto(String username) throws IOException {
        ResponseEntity<String> getUrl = restTemplate.exchange(
                getUrlForDownload(username),
                HttpMethod.GET,
                createEntityWithOauth(),
                String.class);
        InputStream fileStream = new URL(getUrlForDownload(username)).openStream();
        return new InputStreamResource(fileStream);
    }

    public void postFile(User user, MultipartFile file) throws IOException {
        String username = user.getUsername();
        if (user.getPhoto() != null) {
            uploadFile(username, file, getUrlForUpload(username));
        } else {
            deleteFile(username);
            uploadFile(username,file,getUrlForUpload(username));
        }
    }
}
