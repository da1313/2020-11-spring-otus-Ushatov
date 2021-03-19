package org.course.repositories;

import org.course.api.pojo.BookCount;
import org.course.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveSortingRepository<Comment, String> {

    Flux<Comment> findByBookId(String bookId, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: { 'book._id' : ObjectId('?0')} }",
            "{ $count : 'value' }"
    })
    Mono<BookCount> findCountByBook(String bookId);

    void deleteByBookId(String bookId);

    void deleteByUserId(String userId);

}
