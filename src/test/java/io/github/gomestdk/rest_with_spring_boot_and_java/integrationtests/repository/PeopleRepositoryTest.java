package io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.repository;

import io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.testcontainers.AbstractIntegrationTest;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.People;
import io.github.gomestdk.rest_with_spring_boot_and_java.repository.PeopleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PeopleRepository repository;
    private static People person;

    @BeforeAll
    static void setUp() {
        person = new People();
    }

    @Test
    @Order(1)
    void findPeopleByName() {
        Pageable pageable = PageRequest.of(
                0,
                12,
                Sort.by(Sort.Direction.ASC, "firstName"));

        person = repository.findPeopleByName("iko", pageable).getContent().get(0);

        assertNotNull(person);
        assertEquals("Nikola", person.getFirstName());
        assertEquals("Tesla", person.getLastName());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    void disablePerson() {

        Long id = person.getId();
        repository.disablePerson(id);

        var result = repository.findById(id);
        person = result.get();

        assertNotNull(person);
        assertEquals("Nikola", person.getFirstName());
        assertEquals("Tesla", person.getLastName());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());
    }
}