package org.course.service;

import lombok.AllArgsConstructor;
import org.course.dao.intefaces.AuthorDao;
import org.course.dao.intefaces.BookDao;
import org.course.dao.intefaces.CategoryDao;
import org.course.dao.intefaces.GenreDao;
import org.course.service.intefaces.CountEntityService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CountEntityServiceImpl implements CountEntityService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final CategoryDao categoryDao;

    @Override
    public String getCount(String tableName) {
        switch (tableName) {
            case "authors":
                return String.valueOf(authorDao.count());
            case "books":
                return String.valueOf(bookDao.count());
            case "genres":
                return String.valueOf(genreDao.count());
            case "categories":
                return String.valueOf(categoryDao.count());
        }
        return null;
    }
}
