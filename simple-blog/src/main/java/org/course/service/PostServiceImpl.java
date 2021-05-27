package org.course.service;

import lombok.RequiredArgsConstructor;
import org.course.api.request.PostRequest;
import org.course.api.response.*;
import org.course.exception.DocumentNotFoundException;
import org.course.mapping.PostToPostByUserMapper;
import org.course.mapping.PostToPostCardDataMapper;
import org.course.model.ModerationStatus;
import org.course.model.Post;
import org.course.model.User;
import org.course.model.UserPostStatus;
import org.course.model.embedded.PostStatistics;
import org.course.model.embedded.UserShort;
import org.course.repository.PostRepository;
import org.course.repository.UserRepository;
import org.course.repository.VoteRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    private final PostToPostCardDataMapper postToPostCardDataMapper = Mappers.getMapper(PostToPostCardDataMapper.class);
    private final PostToPostByUserMapper postToPostByUserMapper = Mappers.getMapper(PostToPostByUserMapper.class);

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
    public PostCardDataListResponse getPosts(Integer pageNumber, Integer pageSize, String sort) {

        Page<Post> postPage = postRepository.findByIsActiveAndModerationStatusAndPublicationTimeLessThanEqual(
                PageRequest.of(pageNumber, pageSize, Sort.by(sort).ascending()), true,
                ModerationStatus.ACCEPTED, LocalDateTime.now());
        List<PostCartData> postCartDataList = postPage.getContent().stream().map(postToPostCardDataMapper::sourceToDestination)
                .collect(Collectors.toList());
        return new PostCardDataListResponse(postCartDataList, postPage.getTotalPages());

    }

    //todo add sort?
    public PostListResponse getPostsByStatus(Integer pageNumber, Integer pageSize) {

        Page<Post> postPage = postRepository.findByIsActiveAndModerationStatusAndPublicationTimeLessThanEqual(
                PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending()), true,
                ModerationStatus.NEW, LocalDateTime.now());
        return new PostListResponse(postPage.getContent(), postPage.getTotalPages());

    }


    //todo add text sanitize
    public GenericResponse createPost(PostRequest request) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("User with username %s not found", userId)));
        Post post = new Post(null, request.getTitle(), request.getText(), LocalDateTime.now(),
                new UserShort(user.getId(), user.getFirstName(), user.getLastName(), user.getAvatarUrl()), new PostStatistics(),
                ModerationStatus.NEW, request.getPublicationTime(), request.getIsActive(), request.getPostCardImageUrl());
        Post savedPost = postRepository.save(post);
        return new GenericResponse(true);
    }

//    @Transactional
    public GenericResponse updatePost(String postId, PostRequest request) {
        Post updatedPost = postRepository.findById(postId).map(post -> {
            if (request.getText() != null) post.setText(request.getText());
            if (request.getTitle() != null) post.setTitle(request.getTitle());
            if (request.getPublicationTime() != null) post.setPublicationTime(request.getPublicationTime());
            if (request.getIsActive() != null) post.setIsActive(request.getIsActive());
            if (request.getText() != null || request.getTitle() != null || request.getPostCardImageUrl() != null) {
                post.setModerationStatus(ModerationStatus.NEW);
            }
            return post;
        }).orElseThrow(() -> new DocumentNotFoundException("Post with id " + postId + " not found!"));
        postRepository.save(updatedPost);
        return new GenericResponse(true);
    }

    public Object changeStatus(String postId, ModerationStatus moderationStatus) {

        Post updatedPost = postRepository.findById(postId).map(post -> {
            post.setModerationStatus(moderationStatus);
            return post;
        }).orElseThrow(() -> new DocumentNotFoundException("Post with id " + postId + " not found!"));
        postRepository.save(updatedPost);
        return true;

    }
    //todo on moderation post is blocked
    //todo rework to poly
    public PostByUserListResponse getUserPosts(Integer pageNumber, Integer pageSize, String sort, UserPostStatus status) {

        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("User with username %s not found", id)));

        if (status.equals(UserPostStatus.MODERATED)){
            Page<Post> postPage = postRepository.findByUserIdAndIsActiveAndModerationStatusAndPublicationTimeLessThanEqual(
                    PageRequest.of(pageNumber, pageSize), user.getId(), true, ModerationStatus.NEW, LocalDateTime.now());
            List<PostByUser> postByUserList = postPage.getContent().stream().map(postToPostByUserMapper::sourceToDestination)
                    .collect(Collectors.toList());
            return new PostByUserListResponse(postByUserList, pageNumber, (int) postPage.getTotalElements());
        } else if (status.equals(UserPostStatus.DECLINED)){
            Page<Post> postPage = postRepository.findByUserIdAndIsActiveAndModerationStatusAndPublicationTimeLessThanEqual(
                    PageRequest.of(pageNumber, pageSize), user.getId(), true, ModerationStatus.DECLINED, LocalDateTime.now());
            List<PostByUser> postByUserList = postPage.getContent().stream().map(postToPostByUserMapper::sourceToDestination)
                    .collect(Collectors.toList());
            return new PostByUserListResponse(postByUserList, pageNumber, (int) postPage.getTotalElements());
        } else if (status.equals(UserPostStatus.PUBLISHED)){
            Page<Post> postPage = postRepository.findByUserIdAndIsActiveAndModerationStatusAndPublicationTimeLessThanEqual(
                    PageRequest.of(pageNumber, pageSize), user.getId(), true, ModerationStatus.ACCEPTED, LocalDateTime.now());
            List<PostByUser> postByUserList = postPage.getContent().stream().map(postToPostByUserMapper::sourceToDestination)
                    .collect(Collectors.toList());
            return new PostByUserListResponse(postByUserList, pageNumber, postPage.getTotalElements());
        } else if (status.equals(UserPostStatus.INACTIVE)){
            Page<Post> postPage = postRepository.findByUserIdAndIsActive(PageRequest.of(pageNumber, pageSize),
                    user.getId(), false);
            List<PostByUser> postByUserList = postPage.getContent().stream().map(postToPostByUserMapper::sourceToDestination)
                    .collect(Collectors.toList());
            return new PostByUserListResponse(postByUserList, pageNumber, postPage.getTotalPages());
        } else if (status.equals(UserPostStatus.FUTURE)){
            Page<Post> postPage = postRepository.findByUserIdAndPublicationTimeGreaterThan(
                    PageRequest.of(pageNumber, pageSize), user.getId(),  LocalDateTime.now());
            List<PostByUser> postByUserList = postPage.getContent().stream().map(postToPostByUserMapper::sourceToDestination)
                    .collect(Collectors.toList());
            return new PostByUserListResponse(postByUserList, pageNumber, postPage.getTotalElements());
        } else {//ALL
            Page<Post> postPage = postRepository.findByUserId(PageRequest.of(pageNumber, pageSize), user.getId());
            List<PostByUser> postByUserList = postPage.getContent().stream().map(postToPostByUserMapper::sourceToDestination)
                    .collect(Collectors.toList());
            return new PostByUserListResponse(postByUserList, pageNumber, postPage.getTotalElements());
        }
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
}
