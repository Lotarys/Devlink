package com.lotarys.devlink.dtos;

import com.lotarys.devlink.entities.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {
        private String title;

        private List<Link> links;
}
