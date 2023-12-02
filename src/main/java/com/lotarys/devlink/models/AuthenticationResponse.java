package com.lotarys.devlink.models;

import com.lotarys.devlink.dtos.ResponseCardDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String email;

    private String img;

    private String firstName;

    private String lastName;

    private List<ResponseCardDTO> devCards;
}
