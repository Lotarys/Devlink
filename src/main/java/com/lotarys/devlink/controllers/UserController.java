package com.lotarys.devlink.controllers;

import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.services.PhotoService;
import com.lotarys.devlink.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PhotoService photoService;

    @PostMapping("/photo")
    public ResponseEntity<?> postFile(@AuthenticationPrincipal User user, @RequestParam MultipartFile file) throws IOException {
        photoService.UploadFile(user, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Photo uploaded");
    }

}
