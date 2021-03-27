package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.api.request.CommentRequest;
import org.course.service.interfaces.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public String createComment(CommentRequest request){
        commentService.createComment(request);
        return "redirect:/books/" + request.getBookId();
    }

}
