package org.course.mapping;

import org.course.api.response.PostCartData;
import org.course.model.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostToPostCardDataMapper {

    PostCartData sourceToDestination(Post post);

}