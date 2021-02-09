package org.course;

import lombok.AllArgsConstructor;
import org.course.service.intefaces.AuthorHandlerService;
import org.course.service.intefaces.BookHandlerService;
import org.course.service.intefaces.CommentHandleService;
import org.course.service.intefaces.GenreHandlerService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class ShellController {

    private final AuthorHandlerService authorHandleService;
    private final GenreHandlerService genreHandleService;
    private final BookHandlerService bookHandlerService;
    private final CommentHandleService commentHandleService;

    @ShellMethod(value = "Creates an author with a specified name", key = {"create-author", "ca"})
    public String createAuthor(String name){
        return authorHandleService.createAuthor(name);
    }

    @ShellMethod(value = "Read an author with a specified id", key = {"read-author", "ra"})
    public String readAuthor(String id){
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
    public String updateAuthor(String id, String name){
        try {
            return  authorHandleService.updateAuthor(id, name);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Deletes an author with a specified id", key = {"delete-author", "da"})
    public String deleteAuthor(String id){
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
    public String readGenre(String id){
        try {
            return genreHandleService.readGenre(id);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Read all genres", key = {"read-all-genres", "rag"})
    public String readGenres(int page, int size){
        return genreHandleService.readAllGenres(page, size);
    }

    @ShellMethod(value = "Updates an genre with a specified id", key = {"update-genre", "ug"})
    public String updateGenre(String id, String name){
        try {
            return  genreHandleService.updateGenre(id, name);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Deletes an genre with a specified id", key = {"delete-genre", "dg"})
    public String deleteGenre(String id){
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
    @ShellMethod(value = "Creates the book with a specified name", key = {"create-book", "cb"})
    public String createBook(String name, String authorId, String genreId){
        try {
            return bookHandlerService.createBook(name, authorId, genreId);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Read the book with a specified id", key = {"read-book", "rb"})
    public String readBook(String id, int page, int size){
        try {
            return bookHandlerService.readBook(id, page, size);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Read all books", key = {"read-all-books", "rab"})
    public String readBooks(int page, int size){
        return bookHandlerService.readAllBooks(page, size);
    }

    @ShellMethod(value = "Updates the book with a specified id", key = {"update-book", "ub"})
    public String updateBook(String id, String name){
        try {
            return bookHandlerService.updateBook(id, name);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Deletes the book with a specified id", key = {"delete-book", "db"})
    public String deleteBook(String id){
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

    @ShellMethod(value = "Adds genre to the book", key = {"add-genre", "addg"})
    public String addGenre(String bookId, String genreId){
        try {
            return bookHandlerService.addGenre(bookId, genreId);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Remove genre to the book", key = {"remove-genre", "rmg"})
    public String removeGenre(String bookId, String genreId){
        try {
            return bookHandlerService.removeGenre(bookId, genreId);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }
    //------------------------------------------------------------------------------------------------------------------\
    @ShellMethod(value = "Add comment to the specified book", key = {"create-comment", "cc"})
    public String createBookComment(String bookId, String userId, String text){
        return commentHandleService.createBookComment(bookId, userId, text);
    }

    @ShellMethod(value = "Read the comment with a specified id", key = {"read-comment", "rc"})
    public String readBookComment(String id){
        try {
            return commentHandleService.readBookComment(id);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Read all comments", key = {"read-all-comments", "rac"})
    public String readBookComments(int page, int size){
        return commentHandleService.readAllBookComments(page, size);
    }

    @ShellMethod(value = "Updates the comment with a specified id", key = {"update-comment", "uc"})
    public String updateBookComment(String id, String text){
        try {
            return  commentHandleService.updateBookComment(id, text);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Deletes the comment with a specified id", key = {"delete-comment", "dc"})
    public String deleteBookComment(String id){
        return  commentHandleService.deleteBookComment(id);
    }

    @ShellMethod(value = "Deletes all comments", key = {"delete-all-comments", "dac"})
    public String deleteBookComments(){
        return commentHandleService.deleteAllBookComments();
    }

    @ShellMethod(value = "Read comments count", key = {"count-comments", "ctc"})
    public String countBookComments(){
        return commentHandleService.countBookComments();
    }
}
