package com.lotarys.devlink.controllers;

import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.models.UpdateUserRequest;
import com.lotarys.devlink.services.PhotoService;
import com.lotarys.devlink.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PhotoService photoService;

    //TODO Изменить и добавить изменение фото и ферст нейм и ласт нейм
    @PostMapping("/photo")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal User user, @RequestBody UpdateUserRequest updateUserRequest) throws IOException {
        userService.updateUser(user, updateUserRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User updated");
    }

    @GetMapping("/photo")
    public ResponseEntity<InputStreamResource> getPhoto(@AuthenticationPrincipal User user) throws IOException {
        return ResponseEntity.ok()
                .body(photoService.getPhoto(user.getUsername()));
    }
}