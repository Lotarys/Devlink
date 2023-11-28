package com.lotarys.devlink.dtos;

import com.lotarys.devlink.entities.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCardDTO {
    private Long id;

    private String url;

    private String img;

    private String title;

    private Long views;

    private List<Link> links;
}
