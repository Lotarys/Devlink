package com.lotarys.devlink.controllers;

import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.models.AuthenticationRequest;
import com.lotarys.devlink.models.AuthenticationResponse;
import com.lotarys.devlink.models.RegisterRequest;
import com.lotarys.devlink.models.UserInformationRequest;
import com.lotarys.devlink.services.AuthenticationService;
import com.lotarys.devlink.utils.NotFoundUserException;
import com.lotarys.devlink.utils.UserAlreadyExistException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/me")
    public AuthenticationResponse me(@AuthenticationPrincipal User user, HttpServletRequest request) {
        return authenticationService.Userinfo(request.getHeader("Authorization").substring(7), user);
    }

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<?> handleNotFoundUserException(NotFoundUserException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<?> handleUserAlreadyExistException(UserAlreadyExistException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}
