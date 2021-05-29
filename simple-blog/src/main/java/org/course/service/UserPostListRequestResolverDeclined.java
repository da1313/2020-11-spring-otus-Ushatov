package org.course.service;

import lombok.RequiredArgsConstructor;
import org.course.api.request.RequestParams;
import org.course.api.response.PostByUser;
import org.course.api.response.PostByUserListResponse;
import org.course.configuration.BlogConfiguration;
import org.course.exception.BadConfigurationException;
import org.course.mapping.PostToPostByUserMapper;
import org.course.model.ModerationStatus;
import org.course.model.Post;
import org.course.model.UserPostStatus;
import org.course.repository.PostRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserPostListRequestResolverDeclined implements UserPostListRequestResolver {

    private static final String DECLINED_STATUS = "DECLINED";

    private final PostRepository postRepository;
    private final BlogConfiguration blogConfiguration;

    private final PostToPostByUserMapper postToPostByUserMapper = Mappers.getMapper(PostToPostByUserMapper.class);

    @Override
    public PostByUserListResponse getUserPosts(String userId, RequestParams params) {

        String statusDeclined = blogConfiguration.getModerationStatusList().stream().filter(status -> status.equals(DECLINED_STATUS))
                .findFirst().orElseThrow(() ->
                        new BadConfigurationException(String.format("Cant find status %s in configuration properties", DECLINED_STATUS)));

        Page<Post> postPage = postRepository.findByUserIdAndIsActiveAndModerationStatusAndPublicationTimeLessThanEqual(
                PageRequest.of(params.getPageNumber(), params.getPageSize()), userId, true, statusDeclined, LocalDateTime.now());
        List<PostByUser> postByUserList = postPage.getContent().stream().map(postToPostByUserMapper::sourceToDestination)
                .collect(Collectors.toList());
        return new PostByUserListResponse(postByUserList, params.getPageNumber(), (int) postPage.getTotalElements());
    }

    @Override
    public UserPostStatus getType(){
        return UserPostStatus.DECLINED;
    }

}
