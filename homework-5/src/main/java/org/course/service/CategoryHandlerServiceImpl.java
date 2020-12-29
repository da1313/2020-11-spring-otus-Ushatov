package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.dao.intefaces.BookDao;
import org.course.dao.intefaces.CategoryDao;
import org.course.domain.Book;
import org.course.domain.Category;
import org.course.service.intefaces.CategoryHandlerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryHandlerServiceImpl implements CategoryHandlerService {

    private final CategoryDao categoryDao;
    private final BookDao bookDao;

    @Override
    public String createCategory(String name) {
        Category category = new Category(name);
        categoryDao.create(category);
        return "The category is created!";
    }

    @Override
    public String readCategory(long id) {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        Category category = categoryDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + id + " not found!"));
        table.addRule();
        table.addRow("id", "name", "books");
        table.addRule();
        List<String> collect = category.getBooks()
                .stream().map(b -> "Book(id=" + b.getId() + ", " + "name=" + b.getName() + ")")
                .collect(Collectors.toList());
        table.addRow(category.getId(), category.getName(), collect);
        table.addRule();
        return table.render();
    }

    @Override
    public String readAllCategories() {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        List<Category> categories = categoryDao.findAll();
        table.addRule();
        table.addRow("id", "name", "books");
        table.addRule();
        for (Category category : categories) {
            List<String> collect = category.getBooks()
                    .stream().map(b -> "Book(id=" + b.getId() + ", " + "name=" + b.getName() + ")")
                    .collect(Collectors.toList());
            table.addRow(category.getId(), category.getName(), collect);
            table.addRule();
        }
        return table.render();
    }

    @Override
    public String updateCategory(long id, String name) {
        Category category = categoryDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + id + " not found!"));
        category.setName(name);
        categoryDao.update(category);
        return "The category is updated!";
    }

    @Override
    public String deleteCategory(long id) {
        categoryDao.deleteById(id);
        return "The category is deleted!";
    }

    @Override
    public String deleteAllCategories() {
        categoryDao.delete();
        return "All categories are deleted!";
    }

    @Override
    public String countCategories() {
        return String.valueOf(categoryDao.count());
    }

    @Override
    public String addBook(long categoryId, long bookId) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + categoryId + " not found!"));
        categoryDao.addBook(category, book);
        return "The book is added!";
    }

    @Override
    public String removeBook(long bookId, long categoryId) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + categoryId + " not found!"));
        categoryDao.removeBook(category, book);
        return "The book is removed!";
    }
}
