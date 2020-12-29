package org.course.service.intefaces;

public interface GenreHandlerService {

    String createGenre(String name);

    String readGenre(long id);

    String readAllGenres();

    String updateGenre(long id, String name);

    String deleteGenre(long id);

    String deleteAllGenres();

    String countGenres();

}
