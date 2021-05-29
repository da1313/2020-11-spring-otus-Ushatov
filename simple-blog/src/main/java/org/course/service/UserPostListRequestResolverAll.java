package org.course.service;

import lombok.RequiredArgsConstructor;
import org.course.api.request.RequestParams;
import org.course.api.response.PostByUser;
import org.course.api.response.PostByUserListResponse;
import org.course.configuration.BlogConfiguration;
import org.course.mapping.PostToPostByUserMapper;
import org.course.model.Post;
import org.course.model.UserPostStatus;
import org.course.repository.PostRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserPostListRequestResolverAll implements UserPostListRequestResolver {

    private final PostRepository postRepository;

    private final PostToPostByUserMapper postToPostByUserMapper = Mappers.getMapper(PostToPostByUserMapper.class);

    @Override
    public PostByUserListResponse getUserPosts(String userId, RequestParams params) {
        Page<Post> postPage = postRepository.findByUserId(PageRequest.of(params.getPageNumber(), params.getPageSize()), userId);
        List<PostByUser> postByUserList = postPage.getContent().stream().map(postToPostByUserMapper::sourceToDestination)
                .collect(Collectors.toList());
        return new PostByUserListResponse(postByUserList, params.getPageNumber(), postPage.getTotalElements());
    }

    @Override
    public UserPostStatus getType(){
        return UserPostStatus.ALL;
    }

}
