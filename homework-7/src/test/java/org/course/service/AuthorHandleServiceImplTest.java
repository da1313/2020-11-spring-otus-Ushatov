package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.domain.Author;
import org.course.repository.AuthorRepository;
import org.course.service.AuthorHandlerServiceImpl;
import org.course.service.intefaces.AuthorHandlerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@DisplayName("Class AuthorHandleServiceImpl")
@ExtendWith(MockitoExtension.class)
class AuthorHandleServiceImplTest {

    public static final long AUTHOR_ID = 1L;
    public static final String AUTHOR_NAME = "A1";
    public static final long AUTHOR_COUNT = 1L;
    @Mock
    private AuthorRepository authorRepository;
    @Captor
    private ArgumentCaptor<Author> authorArgumentCaptor;

    @Test
    void shouldPassCorrectAuthorToCreateMethod() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorRepository);
        Author author = new Author();
        author.setName(AUTHOR_NAME);
        service.createAuthor(AUTHOR_NAME);
        Mockito.verify(authorRepository).save(authorArgumentCaptor.capture());
        Assertions.assertThat(authorArgumentCaptor.getValue()).isEqualTo(author);
    }

    @Test
    void shouldThrowExceptionIfAuthorNotFoundOnRead() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorRepository);
        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.readAuthor(AUTHOR_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Author with id " + AUTHOR_ID + " not found!");
    }

    @Test
    void shouldThrowExceptionIfAuthorNotFoundOnUpdate() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorRepository);
        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.updateAuthor(AUTHOR_ID, AUTHOR_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Author with id " + AUTHOR_ID + " not found!");
    }

    @Test
    void shouldPassCorrectAuthorToUpdateMethod() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorRepository);
        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        service.updateAuthor(AUTHOR_ID, AUTHOR_NAME);
        Mockito.verify(authorRepository).save(authorArgumentCaptor.capture());
        Assertions.assertThat(authorArgumentCaptor.getValue()).isEqualTo(author);
    }

    @Test
    void shouldPassCorrectAuthorToDeleteMethod() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorRepository);
        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        service.deleteAuthor(AUTHOR_ID);
        Mockito.verify(authorRepository).delete(authorArgumentCaptor.capture());
        Assertions.assertThat(authorArgumentCaptor.getValue()).isEqualTo(author);
    }

    @Test
    void deleteAllAuthors() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorRepository);
        service.deleteAllAuthors();
        Mockito.verify(authorRepository, Mockito.times(1)).deleteAll();
    }

    @Test
    void countAuthors() {
        AuthorHandlerService service = new AuthorHandlerServiceImpl(authorRepository);
        Mockito.when(authorRepository.count()).thenReturn(AUTHOR_COUNT);
        String actual = service.countAuthors();
        Assertions.assertThat(actual).isEqualTo(String.valueOf(AUTHOR_COUNT));
    }
}