package io.github.gomestdk.rest_with_spring_boot_and_java.services;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.BookDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.RequiredObjectIsNullException;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.Book;
import io.github.gomestdk.rest_with_spring_boot_and_java.repository.BookRepository;
import io.github.gomestdk.rest_with_spring_boot_and_java.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServicesTest {
    MockBook input;

    @InjectMocks
    private BookService bookService;

    @Mock
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByID() {
        Book book = input.mockEntity(1);
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO result = bookService.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(
                result.getLinks().stream().anyMatch(
                        link -> link.getRel().value().equals("self")
                                && link.getHref().endsWith("/api/books/v1/1")
                                && link.getType().equals("GET")
                )
        );

        assertNotNull(
                result.getLinks().stream().anyMatch(
                        link -> link.getRel().value().equals("findAll")
                                && link.getHref().endsWith("/api/books/v1")
                                && link.getType().equals("GET")
                )
        );

        assertNotNull(
                result.getLinks().stream().anyMatch(
                        link -> link.getRel().value().equals("create")
                                && link.getHref().endsWith("/api/books/v1")
                                && link.getType().equals("POST")
                )
        );


        assertNotNull(
                result.getLinks().stream().anyMatch(
                        link -> link.getRel().value().equals("update")
                                && link.getHref().endsWith("/api/books/v1")
                                && link.getType().equals("PUT")
                )
        );


        assertNotNull(
                result.getLinks().stream().anyMatch(
                        link -> link.getRel().value().equals("delete")
                                && link.getHref().endsWith("/api/books/v1/1")
                                && link.getType().equals("DELETE")
                )
        );

        assertEquals("Title Test: 1", result.getTitle());
        assertEquals("Author Test: 1", result.getAuthor());
        assertEquals(1D, Double.parseDouble(String.valueOf(result.getPrice())));
        assertNotNull(result.getLaunchDate());
    }

    @Test
    @Disabled("REASON: STILL UNDER DEVELOPMENT")
    void findAll() {
        List<Book> list = input.mockEntityList();
        when(bookRepository.findAll()).thenReturn(list);
        List<BookDTO> books = new ArrayList<>(); //bookService.findAll(pageable);

        assertNotNull(books);
        assertEquals(14, books.size());

        BookDTO bookOne = books.get(1);

        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getLinks());

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/book/v1/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/book/v1/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Author Test: 1", bookOne.getAuthor());
        assertEquals(1D, Double.parseDouble(String.valueOf(bookOne.getPrice())));
        assertEquals("Title Test: 1", bookOne.getTitle());
        assertNotNull(bookOne.getLaunchDate());

        BookDTO bookFour = books.get(4);

        assertNotNull(bookFour);
        assertNotNull(bookFour.getId());
        assertNotNull(bookFour.getLinks());

        assertNotNull(bookFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/book/v1/4")
                        && link.getType().equals("GET")
                ));

        assertNotNull(bookFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(bookFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(bookFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(bookFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/book/v1/4")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Author Test: 4", bookFour.getAuthor());
        assertEquals(4D, Double.parseDouble(String.valueOf(bookFour.getPrice())));
        assertEquals("Title Test: 4", bookFour.getTitle());
        assertNotNull(bookFour.getLaunchDate());

        BookDTO bookSeven = books.get(7);

        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getId());
        assertNotNull(bookSeven.getLinks());

        assertNotNull(bookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/book/v1/7")
                        && link.getType().equals("GET")
                ));

        assertNotNull(bookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(bookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(bookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(bookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/book/v1/7")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Author Test: 7", bookSeven.getAuthor());
        assertEquals(7D, Double.parseDouble(String.valueOf(bookSeven.getPrice())));
        assertEquals("Title Test: 7", bookSeven.getTitle());
        assertNotNull(bookFour.getLaunchDate());
    }

    @Test
    void create() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        //GAMBIARRA
        when(bookRepository.save(any(Book.class))).thenReturn(persisted);

        BookDTO result = bookService.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(
                result.getLinks().stream().anyMatch(
                        link -> link.getRel().value().equals("self")
                                && link.getHref().endsWith("/api/books/v1/1")
                                && link.getType().equals("GET")
                )
        );

        assertNotNull(
                result.getLinks().stream().anyMatch(
                        link -> link.getRel().value().equals("findAll")
                                && link.getHref().endsWith("/api/books/v1/")
                                && link.getType().equals("GET")
                )
        );

        assertNotNull(
                result.getLinks().stream().anyMatch(
                        link -> link.getRel().value().equals("create")
                                && link.getHref().endsWith("/api/books/v1/1")
                                && link.getType().equals("POST")
                )
        );

        assertNotNull(
                result.getLinks().stream().anyMatch(
                        link -> link.getRel().value().equals("update")
                                && link.getHref().endsWith("/api/books/v1/1")
                                && link.getType().equals("PUT")
                )
        );

        assertNotNull(
                result.getLinks().stream().anyMatch(
                        link -> link.getRel().value().equals("delete")
                                && link.getHref().endsWith("/api/books/v1/1")
                                && link.getType().equals("DELETE")
                )
        );

        assertEquals("Title Test: 1", result.getTitle());
        assertEquals("Author Test: 1", result.getAuthor());
        assertEquals(1D, Double.parseDouble(String.valueOf(result.getPrice())));
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    bookService.create(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(persisted);

        var result = bookService.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/book/v1/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/book/v1/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Title Test: 1", result.getTitle());
        assertEquals("Author Test: 1", result.getAuthor());
        assertEquals(1D, Double.parseDouble(String.valueOf(result.getPrice())));
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    bookService.update(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.delete(1L);
        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).delete(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
    }
}
