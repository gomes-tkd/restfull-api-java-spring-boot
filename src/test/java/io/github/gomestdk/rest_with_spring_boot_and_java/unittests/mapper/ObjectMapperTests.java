package io.github.gomestdk.rest_with_spring_boot_and_java.unittests.mapper;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.People;
import io.github.gomestdk.rest_with_spring_boot_and_java.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.gomestdk.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseListObjects;
import static io.github.gomestdk.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectMapperTests {
    MockPerson inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockPerson();
    }

    @Test
    public void parseEntityToDTOTest() {
        PeopleDTO output = parseObject(inputObject.mockEntity(), PeopleDTO.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("First Name Test: 0", output.getFirstName());
        assertEquals("Last Name Test: 0", output.getLastName());
        assertEquals("Address Test: 0", output.getAddress());
        assertEquals("Gender Test: Male", output.getGender());
    }

    @Test
    public void parseEntityListToDTOListTest() {
        List<PeopleDTO> outList = parseListObjects(inputObject.mockEntityList(), PeopleDTO.class);

        PeopleDTO outputZero = outList.getFirst();
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("First Name Test: 0", outputZero.getFirstName());
        assertEquals("Last Name Test: 0", outputZero.getLastName());
        assertEquals("Address Test: 0", outputZero.getAddress());
        assertEquals("Gender Test: Male", outputZero.getGender());

        PeopleDTO outputSeven = outList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("First Name Test: 7", outputSeven.getFirstName());
        assertEquals("Last Name Test: 7", outputSeven.getLastName());
        assertEquals("Address Test: 7", outputSeven.getAddress());
        assertEquals("Gender Test: Female", outputSeven.getGender());

        PeopleDTO outputTwelve = outList.get(12);
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("First Name Test: 12", outputTwelve.getFirstName());
        assertEquals("Last Name Test: 12", outputTwelve.getLastName());
        assertEquals("Address Test: 12", outputTwelve.getAddress());
        assertEquals("Gender Test: Male", outputTwelve.getGender());
    }

    @Test
    public void parseDTOToEntityTest() {
        People output = parseObject(inputObject.mockDTO(), People.class);

        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("First Name Test: 0", output.getFirstName());
        assertEquals("Last Name Test: 0", output.getLastName());
        assertEquals("Address Test: 0", output.getAddress());
        assertEquals("Gender Test: Male", output.getGender());
    }

    @Test
    public void parserDTOListToEntityListTest() {
        List<People> outputList = parseListObjects(inputObject.mockDTOList(), People.class);

        People outputZero = outputList.getFirst();
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("First Name Test: 0", outputZero.getFirstName());
        assertEquals("Last Name Test: 0", outputZero.getLastName());
        assertEquals("Address Test: 0", outputZero.getAddress());
        assertEquals("Gender Test: Male", outputZero.getGender());

        People outputSeven = outputList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("First Name Test: 7", outputSeven.getFirstName());
        assertEquals("Last Name Test: 7", outputSeven.getLastName());
        assertEquals("Address Test: 7", outputSeven.getAddress());
        assertEquals("Gender Test: Female", outputSeven.getGender());

        People outputTwelve = outputList.get(12);
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("First Name Test: 12", outputTwelve.getFirstName());
        assertEquals("Last Name Test: 12", outputTwelve.getLastName());
        assertEquals("Address Test: 12", outputTwelve.getAddress());
        assertEquals("Gender Test: Male", outputTwelve.getGender());
    }

}
