package org.course.repository.custom;

import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.domain.ScoreNumber;

public interface BookRepositoryCustom {

    void deleteAuthorInBookCollection(String authorId);

    void updateAuthorInBookCollection(Author author);

    void deleteGenreInBookCollectionById(String genreId);

    void updateGenreInBookCollection(Genre genre);

    void addGenre(Book book, Genre genre);

    void removeGenre(Book book, Genre genre);

    void increaseCommentCountById(String bookId);

    void increaseScoreCount(String bookId, ScoreNumber scoreNumber);

}
