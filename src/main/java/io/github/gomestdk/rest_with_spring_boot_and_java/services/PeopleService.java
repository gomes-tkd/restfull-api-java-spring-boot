package io.github.gomestdk.rest_with_spring_boot_and_java.services;

import io.github.gomestdk.rest_with_spring_boot_and_java.controllers.PeopleController;
import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.BadRequestException;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.FileStorageException;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.RequiredObjectIsNullException;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.ResourceNotFoundException;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.contract.FileExporter;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.factory.FileExporterFactory;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.contract.FileImporter;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.factory.FileImporterFactory;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.People;
import io.github.gomestdk.rest_with_spring_boot_and_java.repository.PeopleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static io.github.gomestdk.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PeopleService {

    private final Logger logger = LoggerFactory.getLogger(PeopleService.class.getName());

    @Autowired
    PeopleRepository peopleRepository;

    @Autowired
    PagedResourcesAssembler<PeopleDTO> assembler;

    @Autowired
    FileImporterFactory fileImporter;

    @Autowired
    private FileExporterFactory fileExporter;

    public PagedModel<EntityModel<PeopleDTO>> findAll(Pageable pageable) {
        logger.info("Fetching all people with pagination: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<People> peopleList = peopleRepository.findAll(pageable);

        logger.info("Found {} people", peopleList.getTotalElements());

        return buildPagedModel(pageable, peopleList);
    }

    public PagedModel<EntityModel<PeopleDTO>> findPeopleByName(String firstName, Pageable pageable) {
        logger.info("Searching for people by name: '{}'", firstName);

        Page<People> peopleList = peopleRepository.findPeopleByName(firstName, pageable);

        logger.info("Found {} people with name containing '{}'", peopleList.getTotalElements(), firstName);

        return buildPagedModel(pageable, peopleList);
    }

    public PeopleDTO findById(Long id) {
        logger.info("Fetching person with id={}", id);

        People entity = peopleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Person not found with id={}", id);
                    return new ResourceNotFoundException("No records found for this ID!");
                });

        PeopleDTO dto = parseObject(entity, PeopleDTO.class);
        addHateoasLinks(dto);

        logger.info("Person found: id={}, name={} {}", dto.getId(), dto.getFirstName(), dto.getLastName());

        return dto;
    }

    public Resource exportPage(Pageable pageable, String acceptHeader) {
        logger.info("Exporting all people with pagination: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        List<PeopleDTO> peopleList = peopleRepository.findAll(pageable)
                .map(person -> parseObject(person, PeopleDTO.class))
                .getContent();

        logger.info("Found {} people", peopleList.size());

        try {
            FileExporter exporter = this.fileExporter.getExporter(acceptHeader);
            return exporter.exportFile(peopleList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public PeopleDTO create(PeopleDTO person) {
        if (person == null) {
            logger.error("Attempted to create a null PeopleDTO");
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating person: {} {}", person.getFirstName(), person.getLastName());

        People entity = parseObject(person, People.class);

        PeopleDTO dto = parseObject(peopleRepository.save(entity), PeopleDTO.class);
        addHateoasLinks(dto);

        logger.info("Person created with id={}", dto.getId());

        return dto;
    }

    public List<PeopleDTO> importPeopleDataFromFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Please set a Valid File");
        }

        logger.info("Starting import of People data from file: '{}', size: {} bytes",
                file.getOriginalFilename(), file.getSize());


        try (InputStream inputStream = file.getInputStream()) {
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new IllegalArgumentException("File name cannot be null"));

            FileImporter importer = this.fileImporter.getImporter(fileName);

            List<People> entities = importer.importFile(inputStream).stream()
                    .map(dto -> peopleRepository.save(parseObject(dto, People.class)))
                    .toList();

            return entities.stream()
                    .map(entity -> {
                        PeopleDTO dto = parseObject(entity, PeopleDTO.class);
                        addHateoasLinks(dto);
                        return dto;
                    })
                    .toList();

        } catch (IOException e) {
            logger.error("Error reading the file: '{}'. Error: {}", file.getOriginalFilename(), e.getMessage());

            throw new FileStorageException("Error processing the file due to I/O issue.");

        } catch (Exception e) {
            logger.error("Error importing People data from file: '{}'. Error: {}",
                    file.getOriginalFilename(), e.getMessage());

            throw new FileStorageException("Error processing the file!");
        }
    }

    public PeopleDTO update(PeopleDTO person) {
        if (person == null) {
            logger.error("Attempted to update a null PeopleDTO");
            throw new RequiredObjectIsNullException();
        }

        logger.info("Updating person with id={}", person.getId());

        People entity = peopleRepository.findById(person.getId())
                .orElseThrow(() -> {
                    logger.warn("Person not found for update with id={}", person.getId());
                    return new ResourceNotFoundException("No records found for this ID!");
                });

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        PeopleDTO dto = parseObject(peopleRepository.save(entity), PeopleDTO.class);
        addHateoasLinks(dto);

        logger.info("Person updated: id={}, name={} {}", dto.getId(), dto.getFirstName(), dto.getLastName());

        return dto;
    }

    @Transactional
    public PeopleDTO disablePerson(Long id) {
        logger.info("Disabling person with id={}", id);

        People entity = peopleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Person not found for disable with id={}", id);
                    return new ResourceNotFoundException("No records found for this ID!");
                });

        peopleRepository.disablePerson(id);

        PeopleDTO dto = parseObject(entity, PeopleDTO.class);
        addHateoasLinks(dto);

        logger.info("Person disabled: id={}", dto.getId());

        return dto;
    }

    public void delete(Long id) {
        logger.info("Attempting to delete person with id={}", id);

        People entity = peopleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Person not found for deletion with id={}", id);
                    return new ResourceNotFoundException("No records found for this ID!");
                });

        peopleRepository.delete(entity);

        logger.info("Person successfully deleted: id={}", id);
    }

    private PagedModel<EntityModel<PeopleDTO>> buildPagedModel(Pageable pageable, Page<People> peopleList) {
        Page<PeopleDTO> peopleWithLinks = peopleList.map((People person) -> {
            PeopleDTO dto = parseObject(person, PeopleDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PeopleController.class).findAll(
                        pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort())
                )
        ).withSelfRel();

        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private void addHateoasLinks(PeopleDTO dto) {
        dto.add(linkTo(methodOn(PeopleController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PeopleController.class).findAll(0, 12, "asc")).withRel("findAll")
                .withType("GET"));
        dto.add(linkTo(methodOn(PeopleController.class).findByName("", 1, 12, "asc"))
                .withRel("findByName").withType("GET"));
        dto.add(linkTo(methodOn(PeopleController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PeopleController.class)).slash("importPeopleDataFromFile")
                .withRel("importPeopleDataFromFile").withType("POST"));
        dto.add(linkTo(methodOn(PeopleController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PeopleController.class).disablePerson(dto.getId())).withRel("disable")
                .withType("PATCH"));
        dto.add(linkTo(methodOn(PeopleController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));

        dto.add(linkTo(methodOn(PeopleController.class)
                .exportPage(0, 12, "asc", null))
                .withRel("exportPage")
                .withType("GET")
                .withTitle("Export People"));
    }
}
