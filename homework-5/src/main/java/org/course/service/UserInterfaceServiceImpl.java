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
import org.course.service.intefaces.PrintService;
import org.course.service.intefaces.UserInterfaceService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserInterfaceServiceImpl implements UserInterfaceService {

    private final PrintService printService;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final CategoryDao categoryDao;
    private final BookDao bookDao;

    @Override
    public long getEntityId(String type) {
        while (true){
            printService.printf("Enter " + type +  " id: ");
            String authorIdString = printService.readLine();
            try {
                return Long.parseLong(authorIdString);
            } catch (NumberFormatException e){
                printService.printf("Incorrect input!\n");
            }
        }
    }

    @Override
    public String getAuthorName() {
        printService.printf("Enter author name: ");
        return printService.readLine();
    }

    @Override
    public Author getAuthorByIdNullable(Author oldAuthor) {
        while (true){
            printService.printf("Enter author id, 'null' to set null, 'enter' to save old: ");
            String bookAuthor = printService.readLine();
            if (bookAuthor.equals("null")){
                return null;
            } else if (bookAuthor.isEmpty()){
                return oldAuthor;
            }
            else {
                try {
                    long authorId = Long.parseLong(bookAuthor);
                    Optional<Author> optional = authorDao.findById(authorId);
                    if (optional.isPresent()){
                        return optional.get();
                    } else {
                        printService.printf("Can't find author with id " + authorId + " !\n");
                    }
                } catch (NumberFormatException e){
                    printService.printf("Incorrect input!\n");
                }
            }
        }
    }

    @Override
    public Author getAuthorByIdNullable() {
        while (true){
            printService.printf("Enter author id, 'null' to set null: ");
            String bookAuthor = printService.readLine();
            if (bookAuthor.equals("null")){
                return null;
            } else {
                try {
                    long authorId = Long.parseLong(bookAuthor);
                    Optional<Author> optional = authorDao.findById(authorId);
                    if (optional.isPresent()){
                        return optional.get();
                    } else {
                        printService.printf("Can't find author with id " + authorId + " !\n");
                    }
                } catch (NumberFormatException e){
                    printService.printf("Incorrect input!\n");
                }
            }
        }
    }

    @Override
    public Author getAuthorById() {
        while (true){
            printService.printf("Enter author id: ");
            String authorIdString = printService.readLine();
            try {
                long authorId = Long.parseLong(authorIdString);
                Optional<Author> optional = authorDao.findById(authorId);
                if (optional.isPresent()){
                    return optional.get();
                } else {
                    printService.printf("Can't find author with id " + authorId + " !\n");
                }
            } catch (NumberFormatException e){
                printService.printf("Incorrect input!\n");
            }
        }
    }

    @Override
    public Author getAuthor() {
        printService.printf("Enter author name: ");
        String authorName = printService.readLine();
        Author author = new Author();
        author.setName(authorName);
        return author;
    }

    @Override
    public String getGenreName() {
        printService.printf("Enter genre name: ");
        return printService.readLine();
    }

    @Override
    public Genre getGenreByIdNullable(Genre oldGenre) {
        while (true){
            printService.printf("Enter genre id, 'null' to set null, 'enter' to save old: ");
            String bookGenre = printService.readLine();
            if (bookGenre.equals("null")){
                return null;
            } else if (bookGenre.isEmpty()){
                return oldGenre;
            }
            else {
                try {
                    long genreId = Long.parseLong(bookGenre);
                    Optional<Genre> optional = genreDao.findById(genreId);
                    if (optional.isPresent()){
                        return optional.get();
                    } else {
                        printService.printf("Can't find genre with id " + genreId + " !\n");
                    }
                } catch (NumberFormatException e){
                    printService.printf("Incorrect input!\n");
                }
            }
        }
    }

    @Override
    public Genre getGenreByIdNullable() {
        while (true){
            printService.printf("Enter genre id, 'null' to set null: ");
            String bookGenre = printService.readLine();
            if (bookGenre.equals("null")){
                return null;
            } else {
                try {
                    long genreId = Long.parseLong(bookGenre);
                    Optional<Genre> optional = genreDao.findById(genreId);
                    if (optional.isPresent()){
                        return optional.get();
                    } else {
                        printService.printf("Can't find genre with id " + genreId + " !\n");
                    }
                } catch (NumberFormatException e){
                    printService.printf("Incorrect input!\n");
                }
            }
        }
    }

    @Override
    public Genre getGenreById() {
        while (true){
            printService.printf("Enter genre id: ");
            String genreIdString = printService.readLine();
            try {
                long genreId = Long.parseLong(genreIdString);
                Optional<Genre> optional = genreDao.findById(genreId);
                if (optional.isPresent()){
                    return optional.get();
                } else {
                    printService.printf("Can't find genre with id " + genreId + " !\n");
                }
            } catch (NumberFormatException e){
                printService.printf("Incorrect input!\n");
            }
        }
    }

    @Override
    public Genre getGenre() {
        printService.printf("Enter genre name: ");
        String genreName = printService.readLine();
        Genre genre = new Genre();
        genre.setName(genreName);
        return genre;
    }

    @Override
    public String getCategoryName() {
        printService.printf("Enter genre name: ");
        return printService.readLine();
    }

    @Override
    public Category getCategoryById() {
        while (true){
            printService.printf("Enter category id: ");
            String categoryIdString = printService.readLine();
            try {
                long categoryId = Long.parseLong(categoryIdString);
                Optional<Category> optional = categoryDao.findById(categoryId);
                if (optional.isPresent()){
                    return optional.get();
                } else {
                    printService.printf("Can't find category with id " + categoryId + " !\n");
                }
            } catch (NumberFormatException e){
                printService.printf("Incorrect input!\n");
            }
        }
    }

    @Override
    public Category getCategory() {
        printService.printf("Enter category name: ");
        String categoryName = printService.readLine();
        Category category = new Category();
        category.setName(categoryName);
        return category;
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        while (true){
            printService.printf("Enter categories id, 'enter' to skip: ");
            String bookCategories = printService.readLine();
            if (bookCategories.isEmpty()) return categories;
            String[] tokens = bookCategories.split(",");
            int i = 0;
            while (i < tokens.length){
                try {
                    long categoryId = Long.parseLong(tokens[i].trim());
                    Optional<Category> optional = categoryDao.findById(categoryId);
                    if (optional.isPresent()) {
                        categories.add(optional.get());
                    } else {
                        printService.printf("Can't find category with id " + categoryId + " !\n");
                        categories.clear();
                        break;
                    }
                } catch (NumberFormatException e) {
                    printService.printf("Incorrect input!\n");
                    categories.clear();
                    break;
                }
                i++;
            }
            if (i == tokens.length) return categories;
        }
    }

    @Override
    public List<Category> getCategories(List<Category> oldCategories) {
        List<Category> categories = new ArrayList<>();
        while (true){
            printService.printf("Enter categories id, 'null' to set null, 'enter' to save old: ");
            String bookCategories = printService.readLine();
            if (bookCategories.equals("null")){
                return categories;
            }
            if (bookCategories.isEmpty()) return oldCategories;
            String[] tokens = bookCategories.split(",");
            int i = 0;
            while (i < tokens.length){
                try {
                    long categoryId = Long.parseLong(tokens[i].trim());
                    Optional<Category> optional = categoryDao.findById(categoryId);
                    if (optional.isPresent()) {
                        categories.add(optional.get());
                    } else {
                        printService.printf("Can't find category with id " + categoryId + " !\n");
                        categories.clear();
                        break;
                    }
                } catch (NumberFormatException e) {
                    printService.printf("Incorrect input!\n");
                    categories.clear();
                    break;
                }
                i++;
            }
            if (i == tokens.length) return categories;
        }
    }

    @Override
    public String getBookName() {
        printService.printf("Enter book name: ");
        return printService.readLine();
    }

    public String getBookName(String oldName){
        printService.printf("Enter book name, 'enter' to save old: ");
        String input = printService.readLine();
        return input.isEmpty() ? oldName : input;
    }

    @Override
    public Book getBookById() {
        while (true){
            printService.printf("Enter book id: ");
            String bookIdString = printService.readLine();
            try {
                long bookId = Long.parseLong(bookIdString);
                Optional<Book> optional = bookDao.findById(bookId);
                if (optional.isPresent()){
                    return optional.get();
                } else {
                    printService.printf("Can't find book with id " + bookId + " !\n");
                }
            } catch (NumberFormatException e){
                printService.printf("Incorrect input!\n");
            }
        }
    }
}
