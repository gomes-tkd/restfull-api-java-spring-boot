package io.github.gomestdk.rest_with_spring_boot_and_java.services;


import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.exception.RequiredObjectIsNullException;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.People;
import io.github.gomestdk.rest_with_spring_boot_and_java.repository.PeopleRepository;
import io.github.gomestdk.rest_with_spring_boot_and_java.unittest.mocks.MockPerson;
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
class PeopleServicesTest {

    MockPerson input;

    @InjectMocks
    private PeopleService service;

    @Mock
    PeopleRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {

        People people = input.mockEntity(1);
        people.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(people));

        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/people/v1/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/people/v1/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Address Test: 1", result.getAddress());
        assertEquals("First Name Test: 1", result.getFirstName());
        assertEquals("Last Name Test: 1", result.getLastName());
        assertEquals("Gender Test: Female", result.getGender());
    }

    @Test
    void create() {
        People people = input.mockEntity(1);
        People persisted = people;
        persisted.setId(1L);

        PeopleDTO dto = input.mockDTO(1);

        when(repository.save(people)).thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/people/v1/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/people/v1/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Address Test: 1", result.getAddress());
        assertEquals("First Name Test: 1", result.getFirstName());
        assertEquals("Last Name Test: 1", result.getLastName());
        assertEquals("Gender Test: Female", result.getGender());
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.create(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        People people = input.mockEntity(1);
        People persisted = people;
        persisted.setId(1L);

        PeopleDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(people));
        when(repository.save(people)).thenReturn(persisted);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/people/v1/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/people/v1/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Address Test: 1", result.getAddress());
        assertEquals("First Name Test: 1", result.getFirstName());
        assertEquals("Last Name Test: 1", result.getLastName());
        assertEquals("Gender Test: Female", result.getGender());
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.update(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        People people = input.mockEntity(1);
        people.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(people));

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(People.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Disabled("REASON: Still under development")
    void findAll() {
        List<People> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<PeopleDTO> people = new ArrayList<>(); // service.findAll(pageable);

        assertNotNull(people);
        assertEquals(14, people.size());

        var personOne = people.get(1);

        assertNotNull(personOne);
        assertNotNull(personOne.getId());
        assertNotNull(personOne.getLinks());

        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/people/v1/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/people/v1/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Address Test: 1", personOne.getAddress());
        assertEquals("First Name Test: 1", personOne.getFirstName());
        assertEquals("Last Name Test: 1", personOne.getLastName());
        assertEquals("Gender Test: Female", personOne.getGender());

        var personFour = people.get(4);

        assertNotNull(personFour);
        assertNotNull(personFour.getId());
        assertNotNull(personFour.getLinks());

        assertNotNull(personFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/people/v1/4")
                        && link.getType().equals("GET")
                ));

        assertNotNull(personFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(personFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(personFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(personFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/people/v1/4")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Address Test: 4", personFour.getAddress());
        assertEquals("First Name Test: 4", personFour.getFirstName());
        assertEquals("Last Name Test: 4", personFour.getLastName());
        assertEquals("Gender Test: Male", personFour.getGender());

        var personSeven = people.get(7);

        assertNotNull(personSeven);
        assertNotNull(personSeven.getId());
        assertNotNull(personSeven.getLinks());

        assertNotNull(personSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/people/v1/7")
                        && link.getType().equals("GET")
                ));

        assertNotNull(personSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(personSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(personSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/people/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(personSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/people/v1/7")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Address Test: 7", personSeven.getAddress());
        assertEquals("First Name Test: 7", personSeven.getFirstName());
        assertEquals("Last Name Test: 7", personSeven.getLastName());
        assertEquals("Gender Test: Female", personSeven.getGender());
    }
}