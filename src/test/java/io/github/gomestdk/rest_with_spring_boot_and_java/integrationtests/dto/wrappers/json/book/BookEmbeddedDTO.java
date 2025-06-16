package io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.json.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.dto.BookDTO;

import java.io.Serializable;
import java.util.List;

public class BookEmbeddedDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("books")
    private List<BookDTO> books;

    public BookEmbeddedDTO() {
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }
}
