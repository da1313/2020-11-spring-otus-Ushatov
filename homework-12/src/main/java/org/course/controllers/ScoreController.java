package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.api.request.ScoreRequest;
import org.course.service.interfaces.ScoreService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @PostMapping("/scores")
    public String createScore(ScoreRequest request){
        scoreService.createScore(request);
        return "redirect:/books/" + request.getBookId();
    }

}
