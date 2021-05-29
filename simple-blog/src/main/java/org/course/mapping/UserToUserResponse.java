package org.course.mapping;

import org.course.api.response.UserResponse;
import org.course.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserToUserResponse {

    UserResponse sourceToDestination(User user);

}
