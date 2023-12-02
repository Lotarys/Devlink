package com.lotarys.devlink.controllers;

import com.lotarys.devlink.models.AuthenticationRequest;
import com.lotarys.devlink.models.RegisterRequest;
import com.lotarys.devlink.models.UserInformationModel;
import com.lotarys.devlink.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserInformationModel> register(@RequestBody RegisterRequest request) {
        return ResponseEntity
                .ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserInformationModel> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity
                .ok(authenticationService.authenticate(request));
    }

    @GetMapping("/me")
    public UserInformationModel me(HttpServletRequest request) {
        return new UserInformationModel(request.getHeader("Authorization").substring(7));
    }
}
