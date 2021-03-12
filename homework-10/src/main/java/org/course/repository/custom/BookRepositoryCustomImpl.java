package org.course.repository.custom;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.domain.ScoreNumber;
import org.course.api.pojo.BookShort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;


@AllArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoOperations mongoOperations;

    private final MongoClient mongoClient;

    @Override
    public void deleteAuthorInBookCollection(String authorId) {

        mongoOperations.updateMulti(Query.query(Criteria.where("author.id").is(authorId)),
                new Update().set("author", null), Book.class);

    }

    @Override
    public void updateAuthorInBookCollection(Author author) {

        mongoOperations.updateMulti(Query.query(Criteria.where("author.id").is(author.getId())),
                new Update().set("author", author), Book.class);

    }

    @Override
    public void deleteGenreInBookCollectionById(String genreId) {

        Query allBooksWithGenre = Query.query(Criteria.where("genres").elemMatch(Criteria.where("id").is(genreId)));

        Query genreWithId = Query.query(Criteria.where("id").is(genreId));

        mongoOperations.updateMulti(allBooksWithGenre, new Update().pull("genres", genreWithId), Book.class);

    }

    @Override
    public void updateGenreInBookCollection(Genre genre) {

        Query allBooksWithGenre = Query.query(Criteria.where("genres.id").is(genre.getId()));

        Update genreWithId = new Update().set("genres.$[i]", genre)
                .filterArray(Criteria.where("i._id").is(new ObjectId(genre.getId())));

        mongoOperations.updateMulti(allBooksWithGenre, genreWithId, Book.class);

    }

    @Override
    public void addGenre(Book book, Genre genre) {

        mongoOperations.updateFirst(Query.query(Criteria.where("id").is(book.getId()))
                , new Update().push("genres", genre), Book.class);

    }

    @Override
    public void removeGenre(Book book, Genre genre) {

        mongoOperations.updateFirst(Query.query(Criteria.where("id").is(book.getId()))
                , new Update().pull("genres", genre), Book.class);

    }

    @Override
    public void increaseCommentCountById(String bookId){

        mongoOperations.updateFirst(Query.query(Criteria.where("id").is(bookId)),
                new Update().inc("info.commentCount", 1), Book.class);

    }

    @Override
    public void increaseScoreCount(String bookId, ScoreNumber scoreNumber) {

        Map<ScoreNumber, String> fieldMap = new HashMap<>();
        fieldMap.put(ScoreNumber.SCORE_ONE, "info.scoreOneCount");
        fieldMap.put(ScoreNumber.SCORE_TWO, "info.scoreTwoCount");
        fieldMap.put(ScoreNumber.SCORE_THREE, "info.scoreThreeCount");
        fieldMap.put(ScoreNumber.SCORE_FOUR, "info.scoreFourCount");
        fieldMap.put(ScoreNumber.SCORE_FIVE, "info.scoreFiveCount");

        mongoOperations.updateFirst(Query.query(Criteria.where("id").is(bookId)),
                new Update().inc(fieldMap.get(scoreNumber), 1), Book.class);

    }

    public List<BookShort> findAllBookShortCriteria(Pageable pageable){

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.project("title", "author", "genres", "time")
                        .and("info.commentCount").as("commentsCount")
                        .andExpression("info.scoreOneCount + " +
                                "info.scoreTwoCount + " +
                                "info.scoreThreeCount + " +
                                "info.scoreFourCount + " +
                                "info.scoreFiveCount").as("scoreSum")
                        .andExpression("info.scoreOneCount + " +
                                "info.scoreTwoCount*2 + " +
                                "info.scoreThreeCount*3 + " +
                                "info.scoreFourCount*4 + " +
                                "info.scoreFiveCount*5").as("weightSum"),
                Aggregation.project("title", "author", "genres", "time", "commentsCount")
                        .and("avgScore").applyCondition(ConditionalOperators
                        .when(ComparisonOperators.valueOf("$scoreSum").equalToValue(0))
                        .then(0)
                        .otherwise(ArithmeticOperators.valueOf("$weightSum").divideBy("$scoreSum"))),
                Aggregation.sort(pageable.getSort()),
                Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()),
                Aggregation.limit(pageable.getPageSize()));

        return mongoOperations.aggregate(aggregation, Book.class, BookShort.class).getMappedResults();
    }

    public List<BookShort> findAllBookShortNative(Pageable pageable){

        Document sort = new Document();

        pageable.getSort().forEach(o -> sort.append(o.getProperty(), o.getDirection() == Sort.Direction.DESC ? -1 : 1));

        List<Bson> agg = Arrays.asList(
                Aggregates.project(
                Projections.fields(
                        Projections.include("title", "author", "date", "time", "genres"),
                        Projections.computed("commentsCount", "$info.commentCount"),
                        Projections.computed("scoreSum",
                                new Document("$add", List.of("$info.scoreOneCount",
                                        "$info.scoreTwoCount",
                                        "$info.scoreThreeCount",
                                        "$info.scoreFourCount",
                                        "$info.scoreFiveCount"))),
                        Projections.computed("weightSum",
                                new Document("$add", List.of("$info.scoreOneCount",
                                        new Document("$multiply", List.of(2, "$info.scoreTwoCount")),
                                        new Document("$multiply", List.of(3, "$info.scoreThreeCount")),
                                        new Document("$multiply", List.of(4, "$info.scoreFourCount")),
                                        new Document("$multiply", List.of(5, "$info.scoreFiveCount")) ))))),
                Aggregates.project(
                Projections.fields(
                        Projections.include("title", "author", "date", "genres", "time", "commentsCount"),
                        Projections.computed("avgScore",
                                new Document("$cond",
                                        new Document("if", new Document("$eq", List.of("$scoreSum", 0)))
                                                .append("then", 0)
                                                .append("else", new Document("$divide", List.of("$weightSum", "$scoreSum")))) )
                )),
                Aggregates.sort(sort),
                Aggregates.skip(pageable.getPageNumber() * pageable.getPageSize()),
                Aggregates.limit(pageable.getPageSize())
                );

        List<BookShort> result = new ArrayList<>();

        mongoOperations.getCollection("book")
                .aggregate(agg).forEach(d -> result.add(mongoOperations.getConverter().read(BookShort.class, d)));

        return result;

    }

}
