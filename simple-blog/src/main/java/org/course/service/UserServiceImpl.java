package org.course.service;

import lombok.RequiredArgsConstructor;
import org.course.api.request.RegistrationRequest;
import org.course.api.request.UserRequest;
import org.course.api.response.GenericResponse;
import org.course.api.response.ImageUrl;
import org.course.api.response.RegistrationResponse;
import org.course.api.response.UserResponse;
import org.course.configuration.BlogConfiguration;
import org.course.exception.DocumentNotFoundException;
import org.course.mapping.UserToUserResponse;
import org.course.model.Role;
import org.course.model.User;
import org.course.repository.CommentRepository;
import org.course.repository.PostRepository;
import org.course.repository.RoleRepository;
import org.course.repository.UserRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    private final BlogConfiguration blogConfiguration;

    private final UserToUserResponse userToUserResponse;

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

        Optional<User> userWithEmail = userRepository.findByEmail(request.getEmail());

        if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(userId)){
            return new GenericResponse(false);
        }

        return getResponseWhenValidationsIsCorrect(request, user);
    }

    public RegistrationResponse registration(RegistrationRequest request) {

        if (request.getPassword().length() < 8){
            return new RegistrationResponse(false, false, true);
        }

        return userRepository.findByEmail(request.getEmail())
                .map(user -> new RegistrationResponse(false, true, false))
                .orElseGet(() -> getRegistrationResponse(request));

    }

    public GenericResponse activateNewUser(String token) {

        return userRepository.findByValidationToken(token).map(user -> {
            user.setEnabled(true);
            user.setValidationToken(null);
            userRepository.save(user);
            return new GenericResponse(true);
        }).orElseGet(() -> new GenericResponse(false));

    }

    private RegistrationResponse getRegistrationResponse(RegistrationRequest request) {
        Role role = roleRepository.findByName("user")
                .orElseThrow(() -> new DocumentNotFoundException("Role with name user not found"));

        String url = UUID.randomUUID().toString();

        User user = new User(null, request.getFirstName(), request.getLastName(), null,
                request.getEmail(), passwordEncoder.encode(request.getPassword()), LocalDateTime.now(), role,
                null, url, false);

        userRepository.save(user);

        sendRegistrationEmail(url, request.getEmail());

        return new RegistrationResponse(true, false, false);
    }

    private void sendRegistrationEmail(String url, String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(blogConfiguration.getEmail());
        message.setTo(email);
        message.setSubject("Simple blog account activation");
        message.setText(String.format("To activate your account please follow the link http://%s:3000/user/activate/%s",
                blogConfiguration.getDomain(), url));
        mailSender.send(message);
    }

    private GenericResponse getResponseWhenValidationsIsCorrect(UserRequest request, User user) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setEmail(request.getEmail());

        postRepository.updateUserPosts(user.getId(), user.getFirstName(), user.getLastName(), user.getAvatarUrl());
        commentRepository.updateUserComments(user.getId(), user.getFirstName(), user.getLastName(), user.getAvatarUrl());

        userRepository.save(user);
        return new GenericResponse(true);
    }
}
