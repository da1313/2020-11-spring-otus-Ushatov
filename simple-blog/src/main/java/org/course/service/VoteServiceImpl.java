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

        return voteRepository.findByUserIdAndPostId(userId, post.getId())
                .map(vote -> getResponseWhenUserChangeVote(request, post, vote))
                .orElseGet(() -> getResponseWhenUserCreateVote(request, post, userId));

    }

    private GenericResponse getResponseWhenUserCreateVote(VoteRequest request, Post post, String userId) {
        Vote vote = new Vote(null, request.getValue(), userId, request.getPostId(), null, LocalDateTime.now());
        voteRepository.save(vote);

        updatePostStatisticsWhenNewVoteComes(request, post);

        return new GenericResponse(true);
    }

    private GenericResponse getResponseWhenUserChangeVote(VoteRequest request, Post post, Vote vote) {
        boolean oldVote = vote.getValue();

        if (oldVote == request.getValue()){
            return new GenericResponse(false);
        }

        vote.setValue(request.getValue());
        voteRepository.save(vote);

        updatePostStatisticsWhenVoteChanges(post, oldVote);

        return new GenericResponse(true);
    }

    private void updatePostStatisticsWhenNewVoteComes(VoteRequest request, Post post) {
        if (request.getValue()){
           post.getPostStatistics().addLike();
        } else {
            post.getPostStatistics().addDislike();
        }
        postRepository.save(post);
    }

    private void updatePostStatisticsWhenVoteChanges(Post post, boolean oldVote) {
        if (oldVote){
            post.getPostStatistics().removeLike();
            post.getPostStatistics().addDislike();
        } else {
            post.getPostStatistics().removeDislike();
            post.getPostStatistics().addLike();
        }
        postRepository.save(post);
    }
}
