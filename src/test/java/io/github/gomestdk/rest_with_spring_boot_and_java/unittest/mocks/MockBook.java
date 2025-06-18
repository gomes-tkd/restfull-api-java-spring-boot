package io.github.gomestdk.rest_with_spring_boot_and_java.unittest.mocks;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.BookDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {
    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookDTO mockDTO() {
        return mockDTO(0);
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }

        return books;
    }

    public List<BookDTO> mockDTOList() {
        List<BookDTO> books = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }

        return books;
    }

    public Book mockEntity(Integer number) {
        Book book = new Book();

        book.setId(number.longValue());
        book.setTitle("Title Test: " + number);
        book.setAuthor("Author Test: " + number);
        book.setPrice(Double.parseDouble(String.valueOf(number)));
        book.setLaunchDate(new Date());

        return book;
    }


    public BookDTO mockDTO(Integer number) {
        BookDTO dto = new BookDTO();

        dto.setId(number.longValue());
        dto.setTitle("Title Test: " + number);
        dto.setAuthor("Author Test: " + number);
        dto.setPrice(Double.parseDouble(String.valueOf(number)));
        dto.setLaunchDate(new Date());

        return dto;
    }
}
