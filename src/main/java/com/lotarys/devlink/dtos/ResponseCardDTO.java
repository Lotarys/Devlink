package com.lotarys.devlink.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCardDTO {
    private Long id;

    private String url;

    private String title;

    private Long views;
}
