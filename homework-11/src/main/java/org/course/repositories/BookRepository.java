package org.course.repositories;

import org.course.api.pojo.BookCount;
import org.course.api.pojo.BookShort;
import org.course.domain.Book;
import org.course.repositories.cusom.BookRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookRepository extends ReactiveSortingRepository<Book, String>, BookRepositoryCustom {

    @Aggregation(pipeline = {"{ $project: {" +
            "  title:1," +
            "  author:1," +
            "  time:1," +
            "  genres:1," +
            "  commentsCount: '$info.commentCount'," +
            "  scoreSum: {$add:['$info.scoreOneCount','$info.scoreTwoCount', '$info.scoreThreeCount', '$info.scoreFourCount', '$info.scoreFiveCount']}," +
            "  weightSum: {$add:[{$multiply:['$info.scoreOneCount', 1]}, {$multiply:['$info.scoreTwoCount', 2]},{$multiply:['$info.scoreThreeCount', 3]},{$multiply:['$info.scoreFourCount', 4]},{$multiply:['$info.scoreFiveCount', 5]}]} }}",
            "{ $project: {" +
                    "  title:1," +
                    "  author:1," +
                    "  time:1," +
                    "  genres:1," +
                    "  commentsCount: 1," +
                    "  avgScore: {$cond:{if:{$eq:['$scoreSum', 0]},then:0,else:{$divide:['$weightSum','$scoreSum']} }}" +
                    "  }}"})
    Flux<BookShort> findAllBookShortAuto(Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: {'genres._id' : ObjectId(?0) } }",
            "{ $project: {" +
                    "  title:1," +
                    "  author:1," +
                    "  time:1," +
                    "  genres:1," +
                    "  commentsCount: '$info.commentCount'," +
                    "  scoreSum: {$add:['$info.scoreOneCount','$info.scoreTwoCount', '$info.scoreThreeCount', '$info.scoreFourCount', '$info.scoreFiveCount']}," +
                    "  weightSum: {$add:[{$multiply:['$info.scoreOneCount', 1]}, {$multiply:['$info.scoreTwoCount', 2]},{$multiply:['$info.scoreThreeCount', 3]},{$multiply:['$info.scoreFourCount', 4]},{$multiply:['$info.scoreFiveCount', 5]}]} }}",
            "{ $project: {" +
                    "  title:1," +
                    "  author:1," +
                    "  time:1," +
                    "  genres:1," +
                    "  commentsCount: 1," +
                    "  avgScore: {$cond:{if:{$eq:['$scoreSum', 0]},then:0,else:{$divide:['$weightSum','$scoreSum']} }}" +
                    "  }}"})
    Flux<BookShort> findAllBookShortByGenre(String genreId, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: {'genres._id' : ObjectId(?0) } }",
            "{ $count : 'value' }"
    })
    Mono<BookCount> findCountByGenres(String genreId);

    @Aggregation(pipeline = {
            "{ $match: {" +
                    " $or: [{'author.name': /.*?0.*/ },"+
                    "{'title': /.*?0.*/ }]" +
                    "}}",
            "{ $project: {" +
                    "  title:1," +
                    "  author:1," +
                    "  time:1," +
                    "  genres:1," +
                    "  commentsCount: '$info.commentCount'," +
                    "  scoreSum: {$add:['$info.scoreOneCount','$info.scoreTwoCount', '$info.scoreThreeCount', '$info.scoreFourCount', '$info.scoreFiveCount']}," +
                    "  weightSum: {$add:[{$multiply:['$info.scoreOneCount', 1]}, {$multiply:['$info.scoreTwoCount', 2]},{$multiply:['$info.scoreThreeCount', 3]},{$multiply:['$info.scoreFourCount', 4]},{$multiply:['$info.scoreFiveCount', 5]}]} }}",
            "{ $project: {" +
                    "  title:1," +
                    "  author:1," +
                    "  time:1," +
                    "  genres:1," +
                    "  commentsCount: 1," +
                    "  avgScore: {$cond:{if:{$eq:['$scoreSum', 0]},then:0,else:{$divide:['$weightSum','$scoreSum']} }}" +
                    "  }}"})
    Flux<BookShort> findAllBookShortByQuery(String query, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: {" +
                    " $or: [{'author.name': /.*?0.*/ },"+
                    "{'title': /.*?0.*/ }]" +
                    "}}",
            "{ $count : 'value' }"
    })
    Mono<BookCount> findCountByQuery(String query);


}
