package io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.xml;

import io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.dto.PeopleDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
public class PagedModelPeople implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    public List<PeopleDTO> content;

    public PagedModelPeople() {
    }

    public List<PeopleDTO> getContent() {
        return content;
    }

    public void setContent(List<PeopleDTO> content) {
        this.content = content;
    }
}
