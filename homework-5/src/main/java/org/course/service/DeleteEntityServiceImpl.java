package org.course.service;

import lombok.AllArgsConstructor;
import org.course.dao.intefaces.AuthorDao;
import org.course.dao.intefaces.BookDao;
import org.course.dao.intefaces.CategoryDao;
import org.course.dao.intefaces.GenreDao;
import org.course.service.intefaces.DeleteEntityService;
import org.course.service.intefaces.UserInterfaceService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteEntityServiceImpl implements DeleteEntityService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final CategoryDao categoryDao;
    private final UserInterfaceService userInterfaceService;

    @Override
    public String deleteEntityById(String tableName) {
        switch (tableName) {
            case "authors":
                long author = userInterfaceService.getEntityId("author");
                authorDao.deleteById(author);
                return "Entity deleted!";
            case "books":
                long book = userInterfaceService.getEntityId("book");
                bookDao.deleteById(book);
                return "Entity deleted!";
            case "genres":
                long genre = userInterfaceService.getEntityId("genre");
                genreDao.deleteById(genre);
                return "Entity deleted!";
            case "categories":
                long category = userInterfaceService.getEntityId("category");
                categoryDao.deleteById(category);
                return "Entity deleted!";
        }
        return null;
    }

    @Override
    public String deleteAll(String tableName) {
        switch (tableName) {
            case "authors":
                authorDao.delete();
                return "Entities deleted!";
            case "books":
                bookDao.delete();
                return "Entities deleted!";
            case "genres":
                genreDao.delete();
                return "Entities deleted!";
            case "categories":
                categoryDao.delete();
                return "Entities deleted!";
        }
        return null;
    }
}
