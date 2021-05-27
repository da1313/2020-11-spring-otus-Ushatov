package org.course.controller;

import lombok.RequiredArgsConstructor;
import org.course.api.response.GenericResponse;
import org.course.api.response.PostListResponse;
import org.course.api.request.PostRequest;
import org.course.api.response.PostByUserListResponse;
import org.course.api.response.PostResponse;
import org.course.model.ModerationStatus;
import org.course.model.Post;
import org.course.model.UserPostStatus;
import org.course.service.PostServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;

    // anon user >
    @GetMapping("/posts/{id}")
    public PostResponse getPost(@PathVariable("id") String postId){
        return postService.getById(postId);
    }

    // anon user >
    @GetMapping("/posts")
    public Object getPostList(@RequestParam("pageNumber") Integer pageNumber,
                                                @RequestParam("pageSize") Integer pageSize,
                                                @RequestParam("sort") String sort){
//        throw new NullPointerException();
//        return "???";
        return postService.getPosts(pageNumber, pageSize, sort);
    }

    // moderator user >
    @GetMapping("/moderate/posts")
    public PostListResponse getPostListToModerator(@RequestParam("pageNumber") Integer pageNumber,
                                             @RequestParam("pageSize") Integer pageSize){
        return postService.getPostsByStatus(pageNumber, pageSize);
    }

    // common user >
    @PostMapping("/posts")
    public GenericResponse createNewPost(@RequestBody PostRequest request){
        return postService.createPost(request);
    }

    // common user >
    @PutMapping("/posts/{id}")
    public GenericResponse updatePost(@PathVariable("id") String postId, @RequestBody PostRequest request){
        return postService.updatePost(postId, request);
    }

    // moderator user >
    @PutMapping("/moderate/posts/{id}")
    public Object changeStatus(@PathVariable("id") String postId, ModerationStatus moderationStatus){
        return postService.changeStatus(postId, moderationStatus);
    }

    // common user >
    @GetMapping("/posts/user")
    public PostByUserListResponse getUserPostList(@RequestParam("pageNumber") Integer pageNumber,
                                                  @RequestParam("pageSize") Integer pageSize,
                                                  @RequestParam("sort") String sort,
                                                  @RequestParam("status") UserPostStatus status){
        return postService.getUserPosts(pageNumber, pageSize, sort, status);
    }

    @GetMapping("/posts/user/{id}")
    public Post getUserPostById(@PathVariable("id") String postId){
        return postService.getUserPostById(postId);
    }

    @DeleteMapping("/posts/{id}")
    public GenericResponse deletePost(@PathVariable("id") String postId){
        return postService.deletePost(postId);
    }

}
