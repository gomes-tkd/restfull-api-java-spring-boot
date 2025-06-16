package io.github.gomestdk.rest_with_spring_boot_and_java.unittests.services;

import io.github.gomestdk.rest_with_spring_boot_and_java.controllers.BookController;
import io.github.gomestdk.rest_with_spring_boot_and_java.controllers.PeopleController;
import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.BookDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.RequiredObjectIsNullException;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.ResourceNotFoundException;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.Book;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.People;
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


import static io.github.gomestdk.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseListObjects;
import static io.github.gomestdk.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@Service
public class BookService {
    private final Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    PagedResourcesAssembler<BookDTO> assembler;

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable) {
        logger.info("Finding all books");

//        List<BookDTO> bookList = parseListObjects(bookRepository.findAll(), BookDTO.class);
//        bookList.forEach(this::addHateoasLinks);

        Page<Book> bookList = bookRepository.findAll(pageable);
        Page<BookDTO> bookWithLinks = bookList.map((Book person) -> {
            BookDTO dto = parseObject(person, BookDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BookController.class).findAll(
                        pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort())
                )
        ).withSelfRel();

        return assembler.toModel(bookWithLinks, findAllLink);
    }

    public BookDTO findById(Long id) {
        logger.info("Finding one book by id");

        Book entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        BookDTO dto = parseObject(entity, BookDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public BookDTO create(BookDTO book) {
        if (book == null) {
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating one Book!");

        Book entity = parseObject(book, Book.class);

        BookDTO dto = parseObject(bookRepository.save(entity), BookDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public BookDTO update(BookDTO book) {
        if (book == null) {
            throw new RequiredObjectIsNullException();
        }

        Book entity = bookRepository.findById(book.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        logger.info("Updating one book");

        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setPrice(book.getPrice());
        entity.setLaunchDate(book.getLaunchDate());

        BookDTO dto = parseObject(bookRepository.save(entity), BookDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public void delete(Long id) {
        Book entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records for this ID!"));

        logger.info("Deleting one book");

        bookRepository.delete(entity);
    }

    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findAll(0, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
