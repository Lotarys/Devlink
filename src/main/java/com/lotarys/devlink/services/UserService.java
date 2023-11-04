package com.lotarys.devlink.services;


import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.repositories.UserRepository;
import com.lotarys.devlink.utils.NotFoundUserException;
import com.lotarys.devlink.utils.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundUserException("User with email: " + email + " not found"));
    }

    public void save(User user) {
        if(userRepository.findByEmail(user.getEmail()).isEmpty()) {
            userRepository.save(user);
        } else {
            throw new UserAlreadyExistException("User with this email already exist");
        }
    }
}
