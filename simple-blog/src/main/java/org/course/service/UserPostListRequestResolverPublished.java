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
public class UserPostListRequestResolverPublished implements UserPostListRequestResolver {

    private static final String ACCEPTED_STATUS = "ACCEPTED";

    private final PostRepository postRepository;
    private final BlogConfiguration blogConfiguration;

    private final PostToPostByUserMapper postToPostByUserMapper = Mappers.getMapper(PostToPostByUserMapper.class);

    @Override
    public PostByUserListResponse getUserPosts(String userId,  RequestParams params) {

        String statusAccepted = blogConfiguration.getModerationStatusList().stream().filter(status -> status.equals(ACCEPTED_STATUS))
                .findFirst().orElseThrow(() ->
                        new BadConfigurationException(String.format("Cant find status %s in configuration properties", ACCEPTED_STATUS)));

        Page<Post> postPage = postRepository.findByUserIdAndIsActiveAndModerationStatusAndPublicationTimeLessThanEqual(
                PageRequest.of(params.getPageNumber(), params.getPageSize()), userId, true, statusAccepted, LocalDateTime.now());
        List<PostByUser> postByUserList = postPage.getContent().stream().map(postToPostByUserMapper::sourceToDestination)
                .collect(Collectors.toList());
        return new PostByUserListResponse(postByUserList, params.getPageNumber(), postPage.getTotalElements());
    }

    @Override
    public UserPostStatus getType() {
        return UserPostStatus.PUBLISHED;
    }
}
