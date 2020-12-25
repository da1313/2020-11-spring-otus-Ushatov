package org.course.service;

import org.course.dao.intefaces.AuthorDao;
import org.course.dao.intefaces.BookDao;
import org.course.dao.intefaces.CategoryDao;
import org.course.dao.intefaces.GenreDao;
import org.course.service.intefaces.UserInterfaceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Class DeleteEntityServiceImpl")
@ExtendWith(MockitoExtension.class)
class DeleteEntityServiceImplTest {
    public static final String AUTHORS_TABLE = "authors";
    public static final String BOOKS_TABLE = "books";
    public static final String GENRES_TABLE = "genres";
    public static final String CATEGORY_TABLE = "categories";
    public static final long DELETED_ID = 1L;
    @Mock
    private BookDao bookDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private GenreDao genreDao;
    @Mock
    private CategoryDao categoryDao;
    @Mock
    private UserInterfaceService userInterfaceService;

    private ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

    @Test
    void shouldDeleteAuthor(){
        DeleteEntityServiceImpl service = new DeleteEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        Mockito.when(userInterfaceService.getEntityId("author")).thenReturn(DELETED_ID);
        service.deleteEntityById(AUTHORS_TABLE);
        Mockito.verify(authorDao).deleteById(longArgumentCaptor.capture());
        Assertions.assertEquals(DELETED_ID, longArgumentCaptor.getValue());
    }

    @Test
    void shouldDeleteGenre(){
        DeleteEntityServiceImpl service = new DeleteEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        Mockito.when(userInterfaceService.getEntityId("genre")).thenReturn(DELETED_ID);
        service.deleteEntityById(GENRES_TABLE);
        Mockito.verify(genreDao).deleteById(longArgumentCaptor.capture());
        Assertions.assertEquals(DELETED_ID, longArgumentCaptor.getValue());
    }

    @Test
    void shouldDeleteBook(){
        DeleteEntityServiceImpl service = new DeleteEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        Mockito.when(userInterfaceService.getEntityId("book")).thenReturn(DELETED_ID);
        service.deleteEntityById(BOOKS_TABLE);
        Mockito.verify(bookDao).deleteById(longArgumentCaptor.capture());
        Assertions.assertEquals(DELETED_ID, longArgumentCaptor.getValue());
    }

    @Test
    void shouldDeleteCategory(){
        DeleteEntityServiceImpl service = new DeleteEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        Mockito.when(userInterfaceService.getEntityId("category")).thenReturn(DELETED_ID);
        service.deleteEntityById(CATEGORY_TABLE);
        Mockito.verify(categoryDao).deleteById(longArgumentCaptor.capture());
        Assertions.assertEquals(DELETED_ID, longArgumentCaptor.getValue());
    }

}