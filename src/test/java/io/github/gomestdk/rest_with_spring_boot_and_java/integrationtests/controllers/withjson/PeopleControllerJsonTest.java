package io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.controllers.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gomestdk.rest_with_spring_boot_and_java.config.TestConfigs;
import io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.dto.PeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.json.people.WrapperPeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PeopleDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PeopleDTO();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PeopleDTO createdPerson = objectMapper.readValue(content, PeopleDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        person.setLastName("Benedict Torvalds");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PeopleDTO createdPerson = objectMapper.readValue(content, PeopleDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PeopleDTO createdPerson = objectMapper.readValue(content, PeopleDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PeopleDTO createdPerson = objectMapper.readValue(content, PeopleDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTest() throws JsonProcessingException {

        given(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }


    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        WrapperPeopleDTO wrapper = objectMapper.readValue(content, WrapperPeopleDTO.class);
        var people = wrapper.getEmbedded().getPeople();

        PeopleDTO personOne = people.getFirst();

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Allin", personOne.getFirstName());
        assertEquals("Emmot", personOne.getLastName());
        assertEquals("7913 Lindbergh Way", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertFalse(personOne.getEnabled());

        PeopleDTO personFour = people.get(4);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Alonso", personFour.getFirstName());
        assertEquals("Luchelli", personFour.getLastName());
        assertEquals("9 Doe Crossing Avenue", personFour.getAddress());
        assertEquals("Male", personFour.getGender());
        assertFalse(personFour.getEnabled());
    }

    @Test
    @Order(7)
    void hateoasTest() throws JsonProcessingException {

        Response response = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 6 , "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response();

        String json = response.getBody().asString();

        List<Map<String, Object>> people = response.jsonPath().getList("_embedded.people");

        for (Map<String, Object> person : people) {
            Object linksObj = person.get("_links");

            if (!(linksObj instanceof Map<?, ?> links)) {
                fail("Expected '_links' to be a Map but got: " + (linksObj == null ? "null" : linksObj.getClass().getName()));
                continue;
            }

            assertThat("HATEOAS link 'self' is missing", links, hasKey("self"));
            assertThat("HATEOAS link 'findAll' is missing", links, hasKey("findAll"));
            assertThat("HATEOAS link 'create' is missing", links, hasKey("create"));
            assertThat("HATEOAS link 'update' is missing", links, hasKey("update"));
            assertThat("HATEOAS link 'disable' is missing", links, hasKey("disable"));
            assertThat("HATEOAS link 'delete' is missing", links, hasKey("delete"));

            for (Map.Entry<?, ?> entry : links.entrySet()) {
                String key = String.valueOf(entry.getKey());
                Object valueObj = entry.getValue();

                if (!(valueObj instanceof Map<?, ?> linkMap)) {
                    fail("Expected value of link '" + key + "' to be a Map, but got: " + (valueObj == null ? "null" : valueObj.getClass().getName()));
                    continue;
                }

                String href = String.valueOf(linkMap.get("href"));
                assertThat("HATEOAS link '" + key + "' has an invalid URL", href, matchesPattern("https?://.+/api/person/v1.*"));

                String type = String.valueOf(linkMap.get("type"));
                assertThat("HATEOAS link '" + key + "' has an invalid HTTP method", type, notNullValue());
            }
        }


        Map<String, Object> pageLinks = response.jsonPath().getMap("_links");
        assertThat("Page link 'first' is missing", pageLinks, hasKey("first"));
        assertThat("Page link 'self' is missing", pageLinks, hasKey("self"));
        assertThat("Page link 'next' is missing", pageLinks, hasKey("next"));
        assertThat("Page link 'last' is missing", pageLinks, hasKey("last"));

        Map<String, Object> pageAttributes = response.jsonPath().getMap("page");
        assertThat(pageAttributes.get("size"), is(10));
        assertThat(pageAttributes.get("number"), is(6));

        assertTrue((Integer) pageAttributes.get("totalElements") > 0, "totalElements deve ser maior que 0");
        assertTrue((Integer) pageAttributes.get("totalPages") > 0, "totalPages deve ser maior que 0");
    }

    @Test
    @Order(8)
    void findByNameTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("firstName", "and")
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("findPeopleByName/{firstName}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        WrapperPeopleDTO wrapper = objectMapper.readValue(content, WrapperPeopleDTO.class);
        List<PeopleDTO> people = wrapper.getEmbedded().getPeople();

        PeopleDTO personOne = people.getFirst();

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Alessandro", personOne.getFirstName());
        assertEquals("McFaul", personOne.getLastName());
        assertEquals("5 Lukken Plaza", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertTrue(personOne.getEnabled());

        PeopleDTO personFour = people.get(4);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Brandyn", personFour.getFirstName());
        assertEquals("Grasha", personFour.getLastName());
        assertEquals("96 Mosinee Parkway", personFour.getAddress());
        assertEquals("Male", personFour.getGender());
        assertTrue(personFour.getEnabled());
    }

    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }
}