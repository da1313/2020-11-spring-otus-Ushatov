package org.course.service.intefaces;

public interface CategoryHandlerService {

    String createCategory(String name);

    String readCategory(long id);

    String readAllCategories();

    String updateCategory(long id, String name);

    String deleteCategory(long id);

    String deleteAllCategories();

    String countCategories();

    String addBook(long categoryId, long bookId);

    String removeBook(long bookId, long categoryId);
}
