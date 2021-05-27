package org.course.service;

import lombok.RequiredArgsConstructor;
import org.course.api.request.UserRequest;
import org.course.api.response.GenericResponse;
import org.course.api.response.ImageUrl;
import org.course.api.response.UserResponse;
import org.course.exception.DocumentNotFoundException;
import org.course.mapping.UserToUserResponse;
import org.course.model.User;
import org.course.repository.UserRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;

    private final UserToUserResponse userToUserResponse = Mappers.getMapper(UserToUserResponse.class);;

    public UserResponse getUserById(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("User with username %s not found", userId)));

        return userToUserResponse.sourceToDestination(user);
    }

    public ImageUrl getUserAvatar(String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("User with username %s not found", userId)));
        return new ImageUrl(user.getAvatarUrl());
    }

    public GenericResponse updateUser(String userId, UserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("User with username %s not found", userId)));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return new GenericResponse(true);
    }
}
