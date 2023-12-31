package com.lotarys.devlink.services;

import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.models.AuthenticationRequest;
import com.lotarys.devlink.models.RegisterRequest;
import com.lotarys.devlink.models.UserInformationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserInformationModel register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.save(user);
        String jwtToken = jwtService.generateToken(user);
        return new UserInformationModel(jwtToken);
    }

    public UserInformationModel authenticate(AuthenticationRequest request) {
    UserDetails userDetails = (UserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        ).getPrincipal();
        var jwtToken = jwtService.generateToken(userDetails);
        return new UserInformationModel(jwtToken);
    }
}
