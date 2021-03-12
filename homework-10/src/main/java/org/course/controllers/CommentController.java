package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.api.requests.CommentListRequest;
import org.course.api.requests.CommentRequest;
import org.course.api.responces.CommentListResponse;
import org.course.api.responces.ResultResponse;
import org.course.service.interfaces.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public CommentListResponse getComments(CommentListRequest request){

        return commentService.getComments(request);

    }

    @PostMapping("/comments")
    public ResultResponse createComment(@RequestBody CommentRequest request){

        commentService.createComment(request);

        return new ResultResponse(true);

    }

}
