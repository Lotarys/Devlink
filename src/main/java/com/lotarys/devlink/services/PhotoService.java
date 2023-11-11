package com.lotarys.devlink.services;

import com.lotarys.devlink.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.StringReader;

@Service
public class PhotoService {
    @Value("${yandex.token}")
    private String OAUTH_TOKEN;

    @Autowired
    private RestTemplate restTemplate;

    private String getUrlForUpload(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "OAuth " + OAUTH_TOKEN);
        HttpEntity<String> entityForGetUploadUrl = new HttpEntity<>("parameters", headers);
        String path = username + "_photo";
        ResponseEntity<String> getUrl = restTemplate.exchange(
                "https://cloud-api.yandex.net/v1/disk/resources/upload?path=" + path,
                HttpMethod.GET, entityForGetUploadUrl,
                String.class);
        JsonObject jsonObject = Json.createReader(new StringReader(getUrl.getBody()))
                .readObject();
        return jsonObject.getString("href");
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

    private void deleteFile(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "OAuth " + OAUTH_TOKEN);
        HttpEntity<String> entityForDeleteFile = new HttpEntity<>("parameters", headers);
        String path = username + "_photo";
        ResponseEntity<String> response = restTemplate.exchange("https://cloud-api.yandex.net/v1/disk/resources?path=" + path,
                HttpMethod.DELETE,
                entityForDeleteFile,
                String.class);
    }


    public void UploadFile(User user, MultipartFile file) throws IOException {
        String username = user.getUsername();
        if (user.getPhoto() != null) {
            uploadFile(username, file, getUrlForUpload(username));
        } else {
            deleteFile(username);
            uploadFile(username,file,getUrlForUpload(username));
        }
    }
}
