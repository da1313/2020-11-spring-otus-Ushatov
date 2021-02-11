package org.course.repository.custom;

import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;

public interface BookRepositoryCustom {

    void deleteAuthorInBookCollection(String authorId);

    void updateAuthorInBookCollection(Author author);

    void deleteGenreInBookCollectionById(String genreId);

    void updateGenreInBookCollection(Genre genre);

    void addGenre(Book book, Genre genre);

    void removeGenre(Book book, Genre genre);

    void increaseCommentCountById(String bookId);

    void increaseScoreOneCountById(String bookId);

    void increaseScoreTwoCountById(String bookId);

    void increaseScoreThreeCountById(String bookId);

    void increaseScoreFourCountById(String bookId);

    void increaseScoreFiveCountById(String bookId);

}
