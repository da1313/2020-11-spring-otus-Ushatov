package org.course.controller;

import lombok.RequiredArgsConstructor;
import org.course.api.request.VoteRequest;
import org.course.api.response.GenericResponse;
import org.course.service.VoteServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VoteController {

    private final VoteServiceImpl voteService;

    @PostMapping("/votes")
    public GenericResponse createVote(@RequestBody VoteRequest request){
        return voteService.createVote(request);
    }

}
