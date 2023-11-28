package com.lotarys.devlink.services;


import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.dtos.UserUpdateDTO;
import com.lotarys.devlink.models.UpdateUserResponse;
import com.lotarys.devlink.repositories.UserRepository;
import com.lotarys.devlink.exceptions.NotFoundUserException;
import com.lotarys.devlink.exceptions.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundUserException("User with email: " + email + " not found"));
    }

    @Transactional
    public User save(User user) {
        if(userRepository.findByEmail(user.getEmail()).isEmpty()) {
            user.setPhoto("default");
            userRepository.save(user);
            return user;
        } else {
            throw new UserAlreadyExistException("User with this email already exist");
        }
    }

    @Transactional
    public UpdateUserResponse updateUser(User user, UserUpdateDTO updatedUser) {
        if(updatedUser.getImage() != null) {
            String photoUrl = imageService.postUserImage(user, updatedUser.getImage());
            user.setPhoto(photoUrl);
        }
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        userRepository.save(user);
        return new UpdateUserResponse(imageService.getUserImage(user), updatedUser.getFirstName(), updatedUser.getLastName());
    }
}
