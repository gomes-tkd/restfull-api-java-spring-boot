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
@TestMethodOrder(MethodOrderer.class)
class PeopleRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PeopleRepository peopleRepository;

    private static People people;

    @BeforeAll
    static void setUp() {
        people = new People();
    }

    @Test
    @Order(1)
    void findPeopleByName() {
        Pageable pageable = PageRequest.of(
                0, 12, Sort.by(Sort.Direction.ASC, "firstName")
        );
        people = peopleRepository.findPeopleByName("iko", pageable).getContent().get(1);

        assertNotNull(people);
        assertNotNull(people.getId());
        assertEquals("Nikola", people.getFirstName());
        assertEquals("Tesla", people.getLastName());
        assertEquals("Smiljan - Croatia", people.getAddress());
        assertEquals("Male", people.getGender());
        assertTrue(people.getEnabled());
    }

    @Test
    @Order(2)
    void disablePerson() {
        Long id = people.getId();
        peopleRepository.disablePerson(id);

        var result = peopleRepository.findById(id);
        people = result.get();

        assertNotNull(people);
        assertNotNull(people.getId());
        assertEquals("Nikola", people.getFirstName());
        assertEquals("Tesla", people.getLastName());
        assertEquals("Smiljan - Croatia", people.getAddress());
        assertEquals("Male", people.getGender());
        assertFalse(people.getEnabled());
    }
}