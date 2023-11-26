package com.lotarys.devlink.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResponse {
    private String img;

    private String firstName;

    private String lastName;
}
