package org.course.controller;

import lombok.RequiredArgsConstructor;
import org.course.api.response.CommentListResponse;
import org.course.api.request.CommentRequest;
import org.course.api.response.GenericResponse;
import org.course.model.Comment;
import org.course.service.CommentServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @GetMapping("/comments")
    public CommentListResponse getCommentList(@RequestParam("pageNumber") Integer pageNumber,
                                              @RequestParam("pageSize") Integer pageSize,
                                              @RequestParam("sort") String sort,
                                              @RequestParam("postId") String postId) {
        return commentService.getComments(pageNumber, pageSize, sort, postId);
    }

    @PostMapping("/comments")
    public Comment createComment(@RequestBody CommentRequest request){
        return commentService.createComment(request);
    }

    @PutMapping("/comments/{id}")
    public GenericResponse updateComment(@PathVariable("id") String postId, @RequestBody CommentRequest request){
        return commentService.updateComment(postId, request);
    }


}
