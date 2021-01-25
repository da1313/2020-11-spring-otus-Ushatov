package org.course.service.intefaces;

public interface GenreHandlerService {

    String createGenre(String name);

    String readGenre(long id);

    String readAllGenres(int page, int size);

    String updateGenre(long id, String name);

    String deleteGenre(long id);

    String deleteAllGenres();

    String countGenres();

}
