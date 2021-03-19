package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.service.interfaces.CommentHandleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class CommentController {

    private final CommentHandleService commentHandleService;

    @PostMapping("/comment/{id}")
    public String createComment(@PathVariable("id") long id, @RequestParam("text") String text){
        commentHandleService.createComment(id, text);
        return "redirect:/" + id;
    }

}
