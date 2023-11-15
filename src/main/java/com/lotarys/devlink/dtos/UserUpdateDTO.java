package com.lotarys.devlink.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private MultipartFile photo;

    private String firstName;

    private String lastName;
}
