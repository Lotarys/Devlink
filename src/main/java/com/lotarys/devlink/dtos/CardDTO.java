package com.lotarys.devlink.dtos;

import com.lotarys.devlink.entities.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {
        private String title;

        private String email;

        private MultipartFile img;

        private String firstName;

        private String lastName;

        private List<Link> links = new ArrayList<>();
}
