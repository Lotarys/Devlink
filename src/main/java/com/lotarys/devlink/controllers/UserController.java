package com.lotarys.devlink.controllers;

import com.lotarys.devlink.dtos.UserDTO;
import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.dtos.UserUpdateDTO;
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

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal User user,
                                        @RequestParam("firstName") String firstName,
                                        @RequestParam("lastName") String lastName,
                                        @RequestParam("file") MultipartFile file)  {
        UserUpdateDTO updateUserRequest = new UserUpdateDTO();
        updateUserRequest.setFirstName(firstName);
        updateUserRequest.setLastName(lastName);
        updateUserRequest.setImage(file);
        userService.updateUser(user, updateUserRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User updated");
    }

    @GetMapping("/photo")
    public ResponseEntity<String> getPhoto(@AuthenticationPrincipal User user) {
        return ResponseEntity
                .ok()
                .body(imageService.getImage(user));
    }

    @GetMapping("/info")
    public ResponseEntity<UserDTO> userInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity
                .ok(new UserDTO(user.getFirstName(), user.getLastName()));
    }
}