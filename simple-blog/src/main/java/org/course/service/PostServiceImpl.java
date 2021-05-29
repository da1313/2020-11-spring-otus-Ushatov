package org.course.service;

import org.course.api.request.PostRequest;
import org.course.api.request.RequestParams;
import org.course.api.response.*;
import org.course.configuration.BlogConfiguration;
import org.course.exception.BadConfigurationException;
import org.course.exception.DocumentNotFoundException;
import org.course.mapping.PostToPostByUserMapper;
import org.course.mapping.PostToPostCardDataMapper;
import org.course.model.Post;
import org.course.model.User;
import org.course.model.UserPostStatus;
import org.course.model.embedded.PostStatistics;
import org.course.model.embedded.UserShort;
import org.course.repository.PostRepository;
import org.course.repository.UserRepository;
import org.course.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl {

    private static final String ACCEPTED_STATUS = "ACCEPTED";
    private static final String NEW_STATUS = "NEW";
//    private static final String DECLINED_STATUS = "DECLINED";

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final BlogConfiguration blogConfiguration;

    private final PostToPostCardDataMapper postToPostCardDataMapper;
    private final PostToPostByUserMapper postToPostByUserMapper;

    private final List<UserPostListRequestResolver> userPostListRequestResolverList;

    private Map<UserPostStatus, UserPostListRequestResolver> userPostListRequestResolverMap;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, VoteRepository voteRepository,
                           BlogConfiguration blogConfiguration,
                           PostToPostCardDataMapper postToPostCardDataMapper, PostToPostByUserMapper postToPostByUserMapper,
                           List<UserPostListRequestResolver> userPostListRequestResolverList, Map<UserPostStatus,
            UserPostListRequestResolver> userPostListRequestResolverMap) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.blogConfiguration = blogConfiguration;
        this.postToPostCardDataMapper = postToPostCardDataMapper;
        this.postToPostByUserMapper = postToPostByUserMapper;
        this.userPostListRequestResolverList = userPostListRequestResolverList;
        this.userPostListRequestResolverMap = userPostListRequestResolverMap;

        userPostListRequestResolverList.forEach(resolver -> this.userPostListRequestResolverMap.put(resolver.getType(), resolver));

    }


    //    @Transactional
    public PostResponse getById(String postId) {

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DocumentNotFoundException("Post with id " + postId + " not found!"));

        PostResponse response = new PostResponse();

        if (!userId.equals("anonymousUser") && !post.getUser().getId().equals(userId)){
            post.getPostStatistics().addView();
            postRepository.save(post);
        }

        if (!userId.equals("anonymousUser")){
            voteRepository.findByUserIdAndPostId(userId, postId).ifPresentOrElse(vote -> {
                response.setIsVote(true);
                response.setVoteValue(vote.getValue());
            }, () ->{
                response.setIsVote(false);
            });
        }

        response.setPost(post);

        return response;

    }

    //todo validation
    //todo not implemented atm
    public PostCardDataListResponse getPosts(Integer pageNumber, Integer pageSize, String sort, String direction) {

        String acceptedStatus = getStatusFromConfiguration(ACCEPTED_STATUS);

        Sort completeSort = getCompleteSort(sort, direction);

        Page<Post> postPage = postRepository.findByIsActiveAndModerationStatusAndPublicationTimeLessThanEqual(
                PageRequest.of(pageNumber, pageSize, completeSort), true,
                acceptedStatus, LocalDateTime.now());
        List<PostCartData> postCartDataList = postPage.getContent().stream().map(postToPostCardDataMapper::sourceToDestination)
                .collect(Collectors.toList());
        return new PostCardDataListResponse(postCartDataList, postPage.getTotalPages());

    }

    //todo implement in frontend
    public PostListResponse getPostsByStatus(Integer pageNumber, Integer pageSize) {

        String statusNew = getStatusFromConfiguration(NEW_STATUS);

        Page<Post> postPage = postRepository.findByIsActiveAndModerationStatusAndPublicationTimeLessThanEqual(
                PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending()), true,
                statusNew, LocalDateTime.now());
        return new PostListResponse(postPage.getContent(), postPage.getTotalPages());

    }


    //todo add text sanitize
    public GenericResponse createPost(PostRequest request) {

        String statusNew = getStatusFromConfiguration(NEW_STATUS);

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("User with username %s not found", userId)));

        Post post = new Post(null, request.getTitle(), request.getText(), LocalDateTime.now(),
                new UserShort(user.getId(), user.getFirstName(), user.getLastName(), user.getAvatarUrl()), new PostStatistics(),
                statusNew, request.getPublicationTime(), request.getIsActive(), request.getPostCardImageUrl());

        postRepository.save(post);

        return new GenericResponse(true);
    }

//    @Transactional
    public GenericResponse updatePost(String postId, PostRequest request) {

        String statusNew = getStatusFromConfiguration(NEW_STATUS);

        Post updatedPost = postRepository.findById(postId).map(post -> {
            if (request.getText() != null) post.setText(request.getText());
            if (request.getTitle() != null) post.setTitle(request.getTitle());
            if (request.getPublicationTime() != null) post.setPublicationTime(request.getPublicationTime());
            if (request.getIsActive() != null) post.setIsActive(request.getIsActive());
            if (request.getText() != null || request.getTitle() != null || request.getPostCardImageUrl() != null) {
                post.setModerationStatus(statusNew);
            }
            return post;
        }).orElseThrow(() -> new DocumentNotFoundException("Post with id " + postId + " not found!"));
        postRepository.save(updatedPost);
        return new GenericResponse(true);
    }

    public Object changeStatus(String postId, String moderationStatus) {

        Post updatedPost = postRepository.findById(postId).map(post -> {
            post.setModerationStatus(moderationStatus);
            return post;
        }).orElseThrow(() -> new DocumentNotFoundException("Post with id " + postId + " not found!"));
        postRepository.save(updatedPost);
        return true;

    }
    //todo on moderation post is blocked
    public PostByUserListResponse getUserPosts(Integer pageNumber, Integer pageSize, String sort, UserPostStatus status) {

        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("User with username %s not found", id)));
        //todo move to controller
        RequestParams requestParams = new RequestParams(pageNumber, pageSize, sort, status);

        return userPostListRequestResolverMap.get(status).getUserPosts(user.getId(), requestParams);
    }

//    @Transactional
    public GenericResponse deletePost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DocumentNotFoundException("Post with id " + postId + " not found!"));
        postRepository.delete(post);
        return new GenericResponse(true);
    }

//    @Transactional
    public Post getUserPostById(String postId) {

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DocumentNotFoundException("Post with id " + postId + " not found!"));

        if (!userId.equals("anonymousUser") && !post.getUser().getId().equals(userId)){
            post.getPostStatistics().setViewCount(post.getPostStatistics().getViewCount() + 1);
            postRepository.save(post);
        }

        return post;
    }

    private Sort getCompleteSort(String sort, String direction) {

        if (direction.equals("asc")){
            return Sort.by(sort).ascending();
        } else {
            return Sort.by(sort).descending();
        }
    }

    private String getStatusFromConfiguration(String currentStatus) {
        return blogConfiguration.getModerationStatusList().stream().filter(status -> status.equals(currentStatus))
                .findFirst().orElseThrow(() ->
                        new BadConfigurationException(String.format("Cant find status %s in configuration properties", currentStatus)));
    }
}
