package io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.json.people;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.dto.PeopleDTO;

import java.io.Serializable;
import java.util.List;

public class PeopleEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("people")
    private List<PeopleDTO> people;

    public PeopleEmbeddedDTO() {
    }

    public List<PeopleDTO> getPeople() {
        return people;
    }

    public void setPeople(List<PeopleDTO> people) {
        this.people = people;
    }
}
