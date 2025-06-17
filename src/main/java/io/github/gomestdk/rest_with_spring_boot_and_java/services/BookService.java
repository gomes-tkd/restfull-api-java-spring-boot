package io.github.gomestdk.rest_with_spring_boot_and_java.services;

import io.github.gomestdk.rest_with_spring_boot_and_java.controllers.BookController;
import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.BookDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.RequiredObjectIsNullException;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.ResourceNotFoundException;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.Book;
import io.github.gomestdk.rest_with_spring_boot_and_java.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;


import static io.github.gomestdk.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private final Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    PagedResourcesAssembler<BookDTO> assembler;

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable) {
        logger.info("Fetching all books with pagination: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<Book> bookList = bookRepository.findAll(pageable);
        Page<BookDTO> bookWithLinks = bookList.map((Book person) -> {
            BookDTO dto = parseObject(person, BookDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        logger.info("Found {} books", bookList.getTotalElements());

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BookController.class).findAll(
                        pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort())
                )
        ).withSelfRel();

        return assembler.toModel(bookWithLinks, findAllLink);
    }

    public BookDTO findById(Long id) {
        logger.info("Fetching book with id={}", id);

        Book entity = bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Book not found with id={}", id);
                    return new ResourceNotFoundException("No records found for this ID!");
                });

        BookDTO dto = parseObject(entity, BookDTO.class);
        addHateoasLinks(dto);

        logger.info("Book found: {}", dto.getTitle());

        return dto;
    }

    public BookDTO create(BookDTO book) {
        if (book == null) {
            logger.error("Attempted to create a null BookDTO");
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating new book: {}", book.getTitle());

        Book entity = parseObject(book, Book.class);

        BookDTO dto = parseObject(bookRepository.save(entity), BookDTO.class);
        addHateoasLinks(dto);

        logger.info("Book created with id={}", dto.getId());

        return dto;
    }

    public BookDTO update(BookDTO book) {
        if (book == null) {
            logger.error("Attempted to update a null BookDTO");

            throw new RequiredObjectIsNullException();
        }

        Book entity = bookRepository.findById(book.getId())
                .orElseThrow(() -> {
                    logger.warn("Book not found for update with id={}", book.getId());
                    return new ResourceNotFoundException("No records found for this ID!");
                });

        logger.info("Updating book with id={}", book.getId());

        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setPrice(book.getPrice());
        entity.setLaunchDate(book.getLaunchDate());

        BookDTO dto = parseObject(bookRepository.save(entity), BookDTO.class);
        addHateoasLinks(dto);

        logger.info("Book updated: id={}, title={}", dto.getId(), dto.getTitle());

        return dto;
    }

    public void delete(Long id) {
        logger.info("Attempting to delete book with id={}", id);

        Book entity = bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Book not found for deletion with id={}", id);
                    return new ResourceNotFoundException("No records for this ID!");
                });

        logger.info("Deleting one book");

        bookRepository.delete(entity);
        logger.info("Book successfully deleted: id={}", id);
    }

    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findAll(0, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
