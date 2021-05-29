package org.course.controller;

import lombok.RequiredArgsConstructor;
import org.course.api.request.RegistrationRequest;
import org.course.api.request.UserRequest;
import org.course.api.response.GenericResponse;
import org.course.api.response.ImageUrl;
import org.course.api.response.RegistrationResponse;
import org.course.api.response.UserResponse;
import org.course.service.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable("id") String userId){
        return userService.getUserById(userId);
    }

    @GetMapping("/users/avatar/{id}")
    public ImageUrl getUserAvatar(@PathVariable("id") String userId){
        return userService.getUserAvatar(userId);
    }

    @PutMapping("/users/{id}")
    public GenericResponse updateUser(@PathVariable("id") String userId, @RequestBody UserRequest request){
        return userService.updateUser(userId, request);
    }

    @PostMapping("/users")
    public RegistrationResponse registration(@RequestBody RegistrationRequest request){
        return userService.registration(request);
    }

    @PutMapping("/users/activate/{id}")
    public GenericResponse activateUser(@PathVariable("id") String token){
        return userService.activateNewUser(token);
    }

}
