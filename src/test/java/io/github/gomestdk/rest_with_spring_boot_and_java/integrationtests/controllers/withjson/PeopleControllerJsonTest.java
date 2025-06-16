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
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PeopleControllerJsonTest extends AbstractIntegrationTest {


    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PeopleDTO peopleDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        peopleDTO = new PeopleDTO();
    }
    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockPerson();

        specification = new  RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/people/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(peopleDTO)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        PeopleDTO createdPerson = objectMapper.readValue(content, PeopleDTO.class);
        peopleDTO = createdPerson;

        Assertions.assertTrue(createdPerson.getId() > 0);

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getAddress());

        assertEquals("Lt. Jose", createdPerson.getFirstName());
        assertEquals("Gomes", createdPerson.getLastName());
        assertEquals("Vancouver - Canada", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        Assertions.assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        peopleDTO.setLastName("Gomes G.");

        String content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(peopleDTO)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                .   contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        PeopleDTO createdPerson = objectMapper.readValue(content, PeopleDTO.class);
        peopleDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        Assertions.assertTrue(createdPerson.getId() > 0);

        assertEquals("Lt. Jose", createdPerson.getFirstName());
        assertEquals("Gomes G.", createdPerson.getLastName());
        assertEquals("Vancouver - Canada", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        Assertions.assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        String content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", peopleDTO.getId())
                .when().get("{id}")
                .then().statusCode(200).contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        PeopleDTO createdPerson = objectMapper.readValue(content, PeopleDTO.class);
        peopleDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        Assertions.assertTrue(createdPerson.getId() > 0);

        assertEquals("Lt. Jose", createdPerson.getFirstName());
        assertEquals("Gomes G.", createdPerson.getLastName());
        assertEquals("Vancouver - Canada", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        Assertions.assertTrue(createdPerson.getEnabled());
    }


    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", peopleDTO.getId())
                .when().patch("{id}")
                .then().statusCode(200).contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        PeopleDTO createdPerson = objectMapper.readValue(content, PeopleDTO.class);
        peopleDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        Assertions.assertTrue(createdPerson.getId() > 0);

        assertEquals("Lt. Jose", createdPerson.getFirstName());
        assertEquals("Gomes G.", createdPerson.getLastName());
        assertEquals("Vancouver - Canada", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    // void deleteTest() {
    void deleteTest() throws JsonProcessingException {
        given(specification)
            .pathParam("id", peopleDTO.getId())
            .when().delete("{id}")
            .then().statusCode(204);
    }

    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {
        String content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("page", 3, "size", 12, "direction", "asc")
                .when().get()
                .then().statusCode(200).contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        WrapperPeopleDTO wrapperPeopleDTO = objectMapper.readValue(content, WrapperPeopleDTO.class);
        List<PeopleDTO> people = wrapperPeopleDTO.getEmbeddedDTO().getPeople();

        PeopleDTO personOne = people.getFirst();

        assertNotNull(personOne.getId());
        Assertions.assertTrue(personOne.getId() > 0);

        assertEquals("Ayrton", personOne.getFirstName());
        assertEquals("Senna", personOne.getLastName());
        assertEquals("São Paulo - Brasil", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        Assertions.assertTrue(personOne.getEnabled());

        PeopleDTO personFour = people.get(7);

        assertNotNull(personFour.getId());
        Assertions.assertTrue(personFour.getId() > 0);

        assertEquals("Muhamamd", personFour.getFirstName());
        assertEquals("Ali", personFour.getLastName());
        assertEquals("Kentucky - US", personFour.getAddress());
        assertEquals("Male", personFour.getGender());
        Assertions.assertTrue(personFour.getEnabled());
    }

    @Test
    @Order(7)
    void findByNameTest() throws JsonProcessingException {
        String content = given(specification)
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
        List<PeopleDTO> people = wrapper.getEmbeddedDTO().getPeople();

        PeopleDTO personOne = people.getFirst();

        assertNotNull(personOne.getId());
        Assertions.assertTrue(personOne.getId() > 0);

        assertEquals("Ayrton", personOne.getFirstName());
        assertEquals("Senna", personOne.getLastName());
        assertEquals("São Paulo - Brasil", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        Assertions.assertTrue(personOne.getEnabled());

        PeopleDTO personFour = people.get(7);

        assertNotNull(personFour.getId());
        Assertions.assertTrue(personFour.getId() > 0);

        assertEquals("Muhamamd", personFour.getFirstName());
        assertEquals("Ali", personFour.getLastName());
        assertEquals("Kentucky - US", personFour.getAddress());
        assertEquals("Male", personFour.getGender());
        Assertions.assertTrue(personFour.getEnabled());
    }

    private void mockPerson() {
        peopleDTO.setFirstName("Lt. Jose");
        peopleDTO.setLastName("Gomes");
        peopleDTO.setAddress("Vancouver - Canada");
        peopleDTO.setGender("Male");
        peopleDTO.setEnabled(true);
    }
}