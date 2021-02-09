package org.course.service.intefaces;

public interface GenreHandlerService {

    String createGenre(String name);

    String readGenre(String id);

    String readAllGenres(int page, int size);

    String updateGenre(String id, String name);

    String deleteGenre(String id);

    String deleteAllGenres();

    String countGenres();

}
