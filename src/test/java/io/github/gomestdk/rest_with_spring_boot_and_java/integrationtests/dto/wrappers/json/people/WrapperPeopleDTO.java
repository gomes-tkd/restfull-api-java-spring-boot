package io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.json.people;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WrapperPeopleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PeopleEmbeddedDTO embeddedDTO;

    public WrapperPeopleDTO() {}

    public PeopleEmbeddedDTO getEmbeddedDTO() {
        return embeddedDTO;
    }

    public void setEmbeddedDTO(PeopleEmbeddedDTO embeddedDTO) {
        this.embeddedDTO = embeddedDTO;
    }


}
