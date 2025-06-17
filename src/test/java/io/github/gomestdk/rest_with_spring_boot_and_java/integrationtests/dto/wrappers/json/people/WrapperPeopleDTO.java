package io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.json.people;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

public class WrapperPeopleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PeopleEmbeddedDTO embedded;

    public WrapperPeopleDTO() {
    }

    public PeopleEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(PeopleEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}
