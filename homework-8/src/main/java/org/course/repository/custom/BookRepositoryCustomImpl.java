package org.course.repository.custom;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


@AllArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoOperations mongoOperations;

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
}
