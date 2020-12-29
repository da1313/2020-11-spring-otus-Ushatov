package org.course;

import lombok.AllArgsConstructor;
import org.course.service.intefaces.AuthorHandlerService;
import org.course.service.intefaces.BookHandlerService;
import org.course.service.intefaces.CategoryHandlerService;
import org.course.service.intefaces.GenreHandlerService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class ShellController {

    private final AuthorHandlerService authorHandleService;
    private final GenreHandlerService genreHandleService;
    private final CategoryHandlerService categoryHandlerService;
    private final BookHandlerService bookHandlerService;

    @ShellMethod(value = "Creates an author with a specified name", key = {"create-author", "ca"})
    public String createAuthor(String name){
        return authorHandleService.createAuthor(name);
    }

    @ShellMethod(value = "Read an author with a specified id", key = {"read-author", "ra"})
    public String readAuthor(long id){
        try {
            return authorHandleService.readAuthor(id);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Read all authors", key = {"read-all-authors", "raa"})
    public String readAuthors(){
        return authorHandleService.readAllAuthors();
    }

    @ShellMethod(value = "Updates an author with a specified id", key = {"update-author", "ua"})
    public String updateAuthor(long id, String name){
        try {
            return  authorHandleService.updateAuthor(id, name);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Deletes an author with a specified id", key = {"delete-author", "da"})
    public String deleteAuthor(long id){
        return  authorHandleService.deleteAuthor(id);
    }

    @ShellMethod(value = "Deletes all authors", key = {"delete-all-authors", "daa"})
    public String deleteAuthors(){
        return authorHandleService.deleteAllAuthors();
    }

    @ShellMethod(value = "Read authors count", key = {"count-authors", "cta"})
    public String countAuthors(){
        return authorHandleService.countAuthors();
    }
    //------------------------------------------------------------------------------------------------------------------
    @ShellMethod(value = "Creates the genre with a specified name", key = {"create-genre", "cg"})
    public String createGenre(String name){
        return genreHandleService.createGenre(name);
    }

    @ShellMethod(value = "Read the genre with a specified id", key = {"read-genre", "rg"})
    public String readGenre(long id){
        try {
            return genreHandleService.readGenre(id);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Read all genres", key = {"read-all-genres", "rag"})
    public String readGenres(){
        return genreHandleService.readAllGenres();
    }

    @ShellMethod(value = "Updates an genre with a specified id", key = {"update-genre", "ug"})
    public String updateGenre(long id, String name){
        try {
            return  genreHandleService.updateGenre(id, name);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Deletes an genre with a specified id", key = {"delete-genre", "dg"})
    public String deleteGenre(long id){
        return  genreHandleService.deleteGenre(id);
    }

    @ShellMethod(value = "Deletes all genres", key = {"delete-all-genres", "dag"})
    public String deleteGenres(){
        return genreHandleService.deleteAllGenres();
    }

    @ShellMethod(value = "Read genres count", key = {"count-genres", "ctg"})
    public String countGenres(){
        return genreHandleService.countGenres();
    }
    //------------------------------------------------------------------------------------------------------------------
    @ShellMethod(value = "Creates the category with a specified name", key = {"create-category", "cc"})
    public String createCategory(String name){
        return categoryHandlerService.createCategory(name);
    }

    @ShellMethod(value = "Read the category with a specified id", key = {"read-category", "rc"})
    public String readCategory(long id){
        try {
            return categoryHandlerService.readCategory(id);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Read all categories", key = {"read-all-categories", "rac"})
    public String readCategories(){
        return categoryHandlerService.readAllCategories();
    }

    @ShellMethod(value = "Updates the category with a specified id", key = {"update-category", "uc"})
    public String updateCategory(long id, String name){
        try {
            return  categoryHandlerService.updateCategory(id, name);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Deletes the category with a specified id", key = {"delete-category", "dc"})
    public String deleteCategory(long id){
        return  categoryHandlerService.deleteCategory(id);
    }

    @ShellMethod(value = "Deletes all categories", key = {"delete-all-categories", "dac"})
    public String deleteCategories(){
        return categoryHandlerService.deleteAllCategories();
    }

    @ShellMethod(value = "Read categories count", key = {"count-categories", "ctc"})
    public String countCategories(){
        return categoryHandlerService.countCategories();
    }

    @ShellMethod(value = "Add the book to the category", key = {"add-book", "addb"})
    public String addBook(long categoryId, long bookId){
        return categoryHandlerService.addBook(categoryId, bookId);
    }

    @ShellMethod(value = "Remove the book to the category", key = {"remove-book", "rmb"})
    public String removeBook(long categoryId, long bookId){
        return categoryHandlerService.removeBook(categoryId, bookId);
    }
    //------------------------------------------------------------------------------------------------------------------
    @ShellMethod(value = "Creates the book with a specified name", key = {"create-book", "cb"})
    public String createBook(String name, String authorId, String genreId){
        try {
            return bookHandlerService.createBook(name, authorId, genreId);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Read the book with a specified id", key = {"read-book", "rb"})
    public String readBook(long id){
        try {
            return bookHandlerService.readBook(id);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Read all books", key = {"read-all-books", "rab"})
    public String readBooks(){
        return bookHandlerService.readAllBooks();
    }

    @ShellMethod(value = "Updates the book with a specified id", key = {"update-book", "ub"})
    public String updateBook(long id, String name, long authorId, String authorName, long genreId, String genreName){
        try {
            return bookHandlerService.updateBook(id, name, authorId, authorName, genreId, genreName);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Deletes the book with a specified id", key = {"delete-book", "db"})
    public String deleteBook(long id){
        return  bookHandlerService.deleteBook(id);
    }

    @ShellMethod(value = "Deletes all books", key = {"delete-all-books", "dab"})
    public String deleteBooks(){
        return bookHandlerService.deleteAllBooks();
    }

    @ShellMethod(value = "Read books count", key = {"count-books", "ctb"})
    public String countBooks(){
        return bookHandlerService.countBooks();
    }

    @ShellMethod(value = "Adds the book to the specified category", key = {"add-category", "addc"})
    public String addCategory(long bookId, long categoryId){
        try {
            return bookHandlerService.addCategory(bookId, categoryId);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Remove the book from the specified category", key = {"remove-category", "rmc"})
    public String removeCategory(long bookId, long categoryId){
        try {
            return bookHandlerService.removeCategory(bookId, categoryId);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }
}
