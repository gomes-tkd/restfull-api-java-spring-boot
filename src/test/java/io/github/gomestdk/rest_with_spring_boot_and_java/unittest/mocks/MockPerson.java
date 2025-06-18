package io.github.gomestdk.rest_with_spring_boot_and_java.unittest.mocks;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.People;

import java.util.ArrayList;
import java.util.List;

public class MockPerson {
    public People mockEntity() {
        return mockEntity(0);
    }

    public PeopleDTO mockDTO() {
        return mockDTO(0);
    }


    public List<People> mockEntityList() {
        List<People> people = new ArrayList<People>();

        for (int i = 0; i < 14; i++) {
            people.add(mockEntity(i));
        }

        return people;
    }

    public List<PeopleDTO> mockDTOList() {
        List<PeopleDTO> people = new ArrayList<>();

        for(int i = 0; i < 14; i++) {
            people.add(mockDTO(i));
        }

        return people;
    }

    public People mockEntity(Integer number) {
        People people = new People();

        people.setId(number.longValue());
        people.setFirstName("First Name Test: " + number);
        people.setLastName("Last Name Test: " + number);
        people.setGender((number % 2 == 0) ? "Gender Test: Male" : "Gender Test: Female");
        people.setAddress("Address Test: " + number);

        return people;
    }


    public PeopleDTO mockDTO(Integer number) {
        PeopleDTO person = new PeopleDTO();

        person.setId(number.longValue());
        person.setFirstName("First Name Test: " + number);
        person.setLastName("Last Name Test: " + number);
        person.setGender((number % 2 == 0) ? "Gender Test: Male" : "Gender Test: Female");
        person.setAddress("Address Test: " + number);

        return person;
    }


}
