package com.lotarys.devlink.services;

import com.lotarys.devlink.exceptions.NotFoundUserException;
import com.lotarys.devlink.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundUserException("User with email: " + email + " not found"));
    }
}
