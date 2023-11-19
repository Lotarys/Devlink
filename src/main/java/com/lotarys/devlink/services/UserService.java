package com.lotarys.devlink.services;


import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.dtos.UserUpdateDTO;
import com.lotarys.devlink.repositories.UserRepository;
import com.lotarys.devlink.exceptions.NotFoundUserException;
import com.lotarys.devlink.exceptions.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PhotoService photoService;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundUserException("User with email: " + email + " not found"));
    }

    @Transactional
    public User save(User user) {
        if(userRepository.findByEmail(user.getEmail()).isEmpty()) {
            user.setPhoto("https://img.freepik.com/free-vector/cute-man-working-laptop-cartoon-vector-icon-illustration-people-technology-icon-concept-isolated_138676-9123.jpg?size=338&ext=jpg&ga=GA1.1.386372595.1698624000&semt=ais");
            userRepository.save(user);
            return user;
        } else {
            throw new UserAlreadyExistException("User with this email already exist");
        }
    }

    @Transactional
    public void updateUser(User user, UserUpdateDTO updatedUser) throws IOException {
        String photoUrl = photoService.postFile(user ,updatedUser.getPhoto());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhoto(photoUrl);
        userRepository.save(user);
    }
}
