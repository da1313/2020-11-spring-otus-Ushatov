package org.course.service;

import lombok.AllArgsConstructor;
import org.course.dao.intefaces.AuthorDao;
import org.course.dao.intefaces.BookDao;
import org.course.dao.intefaces.CategoryDao;
import org.course.dao.intefaces.GenreDao;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Category;
import org.course.domain.Genre;
import org.course.service.intefaces.UpdateEntityService;
import org.course.service.intefaces.UserInterfaceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UpdateEntityServiceImpl implements UpdateEntityService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final CategoryDao categoryDao;
    private final UserInterfaceService userInterfaceService;

    @Override
    public String updateEntity(String tableName) {
        switch (tableName) {
            case "authors":
                Author author = userInterfaceService.getAuthorById();
                String authorName = userInterfaceService.getAuthorName();
                author.setName(authorName);
                authorDao.update(author);
                return "Entity updated!";
            case "books":
                Book book = userInterfaceService.getBookById();
                String bookName = userInterfaceService.getBookName(book.getName());
                Author bookAuthor = userInterfaceService.getAuthorByIdNullable(book.getAuthor());
                Genre bookGenre = userInterfaceService.getGenreByIdNullable(book.getGenre());
                book.setName(bookName);
                book.setAuthor(bookAuthor);
                book.setGenre(bookGenre);
                List<Category> categories = userInterfaceService.getCategories(book.getCategories());
                bookDao.update(book);
                List<Category> before = book.getCategories();
                List<Category> remove = before.stream().filter(c -> !categories.contains(c)).collect(Collectors.toList());
                List<Category> add = categories.stream().filter(c -> !before.contains(c)).collect(Collectors.toList());
                remove.forEach(c -> bookDao.removeCategory(book, c));
                add.forEach(c -> bookDao.addCategory(book, c));
                return "Entity updated!";
            case "genres":
                Genre genre = userInterfaceService.getGenreById();
                String genreName = userInterfaceService.getGenreName();
                genre.setName(genreName);
                genreDao.update(genre);
                return "Entity updated!";
            case "categories":
                Category category = userInterfaceService.getCategoryById();
                String categoryName = userInterfaceService.getCategoryName();
                category.setName(categoryName);
                categoryDao.update(category);
                return "Entity updated!";
        }
        return null;
    }

}
