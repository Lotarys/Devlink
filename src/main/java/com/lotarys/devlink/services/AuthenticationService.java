package com.lotarys.devlink.services;

import com.lotarys.devlink.dtos.ResponseCardDTO;
import com.lotarys.devlink.entities.Card;
import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.models.AuthenticationRequest;
import com.lotarys.devlink.models.AuthenticationResponse;
import com.lotarys.devlink.models.RegisterRequest;
import com.lotarys.devlink.exceptions.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private List<ResponseCardDTO> mapCardToCardDTO(List<Card> cards) {
        return cards.stream()
                .map(card -> new ResponseCardDTO(card.getId(), card.getUrl(), card.getTitle(), card.getViews()))
                .collect(Collectors.toList());
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User newUser = userService.save(user);
        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken,
                newUser.getEmail(),
                imageService.getImage(newUser),
                newUser.getFirstName(),
                newUser.getLastName(),
                mapCardToCardDTO(user.getCards()));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
    UserDetails userDetails = (UserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        ).getPrincipal();
        var jwtToken = jwtService.generateToken(userDetails);
        User user = (User) userDetails;
        return new AuthenticationResponse(jwtToken,
                user.getEmail(),
                imageService.getImage(user),
                user.getFirstName(),
                user.getLastName(),
                mapCardToCardDTO(user.getCards())
                );
    }

    @Transactional
    public AuthenticationResponse Userinfo(String token, User user) {
        if(user == null) {
            throw new NotFoundUserException("User does not exist");
        } else
            return new AuthenticationResponse(
                    token,
                    user.getEmail(),
                    imageService.getImage(user),
                    user.getFirstName(),
                    user.getLastName(),
                    mapCardToCardDTO(user.getCards())
            );
    }
}
