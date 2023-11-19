package com.lotarys.devlink.controllers;

import com.lotarys.devlink.dtos.UserDTO;
import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.dtos.UserUpdateDTO;
import com.lotarys.devlink.services.PhotoService;
import com.lotarys.devlink.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PhotoService photoService;

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal User user, @RequestBody UserUpdateDTO updateUserRequest) throws IOException {
        userService.updateUser(user, updateUserRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User updated");
    }

    @GetMapping("/photo")
    public ResponseEntity<String> getPhoto(@AuthenticationPrincipal User user) {
        return ResponseEntity
                .ok()
                .body(photoService.getPhoto(user.getUsername()));
    }

    @GetMapping("/info")
    public ResponseEntity<UserDTO> userInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity
                .ok(new UserDTO(user.getFirstName(), user.getLastName()));
    }
}