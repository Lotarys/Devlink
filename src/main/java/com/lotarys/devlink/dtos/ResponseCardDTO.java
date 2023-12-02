package com.lotarys.devlink.dtos;

import com.lotarys.devlink.entities.Link;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCardDTO {
    private Long id;

    private String url;

    private String img;

    private String firstName;

    private String lastName;

    private String email;

    private String title;

    private Long views;

    private List<Link> links;
}
