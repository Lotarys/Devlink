package com.lotarys.devlink.controllers;

import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.dtos.UserUpdateDTO;
import com.lotarys.devlink.models.AuthenticationResponse;
import com.lotarys.devlink.services.ImageService;
import com.lotarys.devlink.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ImageService imageService;

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal User user,
                                        @RequestParam("firstName") String firstName,
                                        @RequestParam("lastName") String lastName,
                                        @RequestParam(value = "file", required = false) MultipartFile file)  {
        UserUpdateDTO updateUserRequest = new UserUpdateDTO();
        updateUserRequest.setFirstName(firstName);
        updateUserRequest.setLastName(lastName);
        updateUserRequest.setImage(file);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUser(user, updateUserRequest));
    }

    @GetMapping("/photo")
    public ResponseEntity<String> getPhoto(@AuthenticationPrincipal User user) {
        return ResponseEntity
                .ok()
                .body(imageService.getUserImage(user));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticationResponse> userInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserinfo(user));
    }
}