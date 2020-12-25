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
import org.course.service.intefaces.CreateEntityService;
import org.course.service.intefaces.UserInterfaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CreateEntityServiceImpl implements CreateEntityService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final CategoryDao categoryDao;
    private final UserInterfaceService userInterfaceService;

    @Override
    public String createEntity(String tableName) {
        switch (tableName) {
            case "authors":
                Author author = userInterfaceService.getAuthor();
                authorDao.createAndIncrement(author);
                return "Entity added!";
            case "books":
                Book book = new Book();
                String bookName = userInterfaceService.getBookName();
                book.setName(bookName);
                Author bookAuthor = userInterfaceService.getAuthorByIdNullable();
                Genre bookGenre = userInterfaceService.getGenreByIdNullable();
                book.setAuthor(bookAuthor);
                book.setGenre(bookGenre);
                List<Category> categories = userInterfaceService.getCategories();
                bookDao.createAndIncrement(book);
                if (!categories.isEmpty()){
                    categories.forEach(c -> bookDao.addCategory(book, c));
                }
                return "Entity added!";
            case "genres":
                Genre genre = userInterfaceService.getGenre();
                genreDao.createAndIncrement(genre);
                return "Entity added!";
            case "categories":
                Category category = userInterfaceService.getCategory();
                categoryDao.createAndIncrement(category);
                return "Entity added!";
        }
        return null;
    }
}
