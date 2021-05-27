package org.course.mapping;

import org.course.api.response.PostByUser;
import org.course.model.Post;
import org.mapstruct.Mapper;

@Mapper
public interface PostToPostByUserMapper {

    PostByUser sourceToDestination(Post post);

}
