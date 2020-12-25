package org.course.service.intefaces;

import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Category;
import org.course.domain.Genre;

import java.util.List;

public interface UserInterfaceService {

    long getEntityId(String type);

    String getAuthorName();

    Author getAuthorByIdNullable();

    Author getAuthorByIdNullable(Author oldAuthor);

    Author getAuthorById();

    Author getAuthor();

    String getGenreName();

    Genre getGenreByIdNullable();

    Genre getGenreByIdNullable(Genre oldGenre);

    Genre getGenreById();

    Genre getGenre();

    String getCategoryName();

    Category getCategoryById();

    Category getCategory();

    List<Category> getCategories();

    List<Category> getCategories(List<Category> oldCategories);

    String getBookName();

    String getBookName(String oldName);

    Book getBookById();

}
