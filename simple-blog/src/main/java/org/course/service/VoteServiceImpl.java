package org.course.service;

import lombok.RequiredArgsConstructor;
import org.course.api.request.VoteRequest;
import org.course.api.response.GenericResponse;
import org.course.exception.DocumentNotFoundException;
import org.course.model.Post;
import org.course.model.Vote;
import org.course.repository.PostRepository;
import org.course.repository.VoteRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl {

    private final VoteRepository voteRepository;

    private final PostRepository postRepository;

//    @Transactional
    public GenericResponse createVote(VoteRequest request) {

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new DocumentNotFoundException("Post with id " + request.getPostId() + " not found!"));

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (post.getUser().getId().equals(userId)){
            return new GenericResponse(false);
        }

        return voteRepository.findByUserIdAndPostId(userId, post.getId()).map(vote -> {
            boolean oldVote = vote.getValue();
            if (oldVote == request.getValue()){
                return new GenericResponse(false);
            }
            vote.setValue(request.getValue());
            voteRepository.save(vote);
            if (oldVote){
                post.getPostStatistics().removeLike();
                post.getPostStatistics().addDislike();
            } else {
                post.getPostStatistics().removeDislike();
                post.getPostStatistics().addLike();
            }
            postRepository.save(post);
            return new GenericResponse(true);
        }).orElseGet(() ->{
            Vote vote = new Vote(null, request.getValue(), userId, request.getPostId(), null, LocalDateTime.now());
            voteRepository.save(vote);
            if (request.getValue()){
               post.getPostStatistics().addLike();
            } else {
                post.getPostStatistics().addDislike();
            }
            postRepository.save(post);
            return new GenericResponse(true);
        });
    }
}
