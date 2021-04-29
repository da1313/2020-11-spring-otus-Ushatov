package org.course;

import lombok.RequiredArgsConstructor;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.domain.User;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.GenreRepository;
import org.course.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitRunner implements ApplicationRunner {

    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<Author> authorList = new ArrayList<>();
        List<Genre> genreList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        List<Book> bookList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Author author = new Author(null, "Author" + i);
            Author save = authorRepository.save(author);
            authorList.add(save);
        }

        for (int i = 0; i < 3; i++) {
            User user = new User(null, "User" + i, "$2y$12$o1fLUVoXyZyivedsnEZYFu6HSIcDPzAwosQjtp75XUadoa0k.HDzG");
            User save = userRepository.save(user);
            userList.add(save);
        }

        for (int i = 0; i < 3; i++) {
            Genre genre = new Genre(null, "Genre" + i);
            Genre save = genreRepository.save(genre);
            genreList.add(save);
        }

        Book book1 = new Book(null, "Title1", authorList.get(0), genreList.get(0), 3);
        Book book2 = new Book(null, "Title2", authorList.get(0), genreList.get(0), 2);
        Book book3 = new Book(null, "Title3", authorList.get(0), genreList.get(1), 0);
        Book book4 = new Book(null, "Title4", authorList.get(1), genreList.get(0), 6);
        Book book5 = new Book(null, "Title5", authorList.get(2), genreList.get(1), 1);

        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        bookList.add(book4);
        bookList.add(book5);

        bookRepository.saveAll(bookList);

    }
}
