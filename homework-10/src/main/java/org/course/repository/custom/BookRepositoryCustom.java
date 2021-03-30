package org.course.repository.custom;

import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.domain.ScoreNumber;
import org.course.api.pojo.BookShort;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepositoryCustom {

    void deleteAuthorInBookCollection(String authorId);

    void updateAuthorInBookCollection(Author author);

    void deleteGenreInBookCollectionById(String genreId);

    void updateGenreInBookCollection(Genre genre);

    void addGenre(Book book, Genre genre);

    void removeGenre(Book book, Genre genre);

    void increaseCommentCountById(String bookId);

    void increaseScoreCount(String bookId, ScoreNumber scoreNumber);

    List<BookShort> findAllBookShortCriteria(Pageable pageable);

    List<BookShort> findAllBookShortNative(Pageable pageable);

}
