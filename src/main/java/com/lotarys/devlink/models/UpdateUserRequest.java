package com.lotarys.devlink.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private MultipartFile photo;
    private String firstName;
    private String lastName;
}
