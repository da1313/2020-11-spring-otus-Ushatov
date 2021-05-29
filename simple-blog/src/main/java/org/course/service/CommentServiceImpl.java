package org.course.service;

import lombok.RequiredArgsConstructor;
import org.course.api.request.CommentRequest;
import org.course.api.response.CommentListResponse;
import org.course.api.response.GenericResponse;
import org.course.configuration.BlogConfiguration;
import org.course.exception.BadConfigurationException;
import org.course.exception.DocumentNotFoundException;
import org.course.exception.IllegalCommentAttemptException;
import org.course.model.Comment;
import org.course.model.ModerationStatus;
import org.course.model.Post;
import org.course.model.User;
import org.course.model.embedded.UserShort;
import org.course.repository.CommentRepository;
import org.course.repository.PostRepository;
import org.course.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl {

    private static final String ACCEPTED_STATUS = "ACCEPTED";

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final BlogConfiguration blogConfiguration;

    public CommentListResponse getComments(Integer pageNumber, Integer pageSize, String sort, String postId) {

        Page<Comment> commentPage = commentRepository.findByPostId(PageRequest.of(pageNumber, pageSize,
                Sort.by(sort).ascending()), postId);

        return new CommentListResponse(commentPage.getContent(), commentPage.getTotalPages());
    }

    //todo sanitize text
//    @Transactional
    public Comment createComment(CommentRequest request) {

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new DocumentNotFoundException("Post with id " + request.getPostId() + " not found!"));

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("User with username %s not found", userId)));

        String acceptedStatus = blogConfiguration.getModerationStatusList().stream().filter(status -> status.equals(ACCEPTED_STATUS))
                .findFirst().orElseThrow(() ->
                        new BadConfigurationException(String.format("Cant find status %s in configuration properties", ACCEPTED_STATUS)));

        if (!post.getModerationStatus().equals(acceptedStatus) ||
                post.getIsActive().equals(false) ||
                post.getPublicationTime().isAfter(LocalDateTime.now())){
            throw new IllegalCommentAttemptException(String.format("User %s, post %s, status %s, is_active %s, pub_time %s, issued at %s",
                    userId, post.getId(), post.getModerationStatus(), post.getIsActive(), post.getPublicationTime(), LocalDateTime.now()));
        }

        Comment comment = new Comment(null, LocalDateTime.now(), request.getText(), new UserShort(user.getId(),
                user.getFirstName(), user.getLastName(), user.getAvatarUrl()), request.getPostId(), null,
                true, 0, 0);

        post.getPostStatistics().addComment();

        Comment savedComment = commentRepository.save(comment);

        postRepository.save(post);

        return savedComment;

    }

//    @Transactional
    public GenericResponse updateComment(String commentId, CommentRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("Comment with id %s not found", commentId)));

        comment.setText(request.getText());

        commentRepository.save(comment);

        return new GenericResponse(true);
    }
}
