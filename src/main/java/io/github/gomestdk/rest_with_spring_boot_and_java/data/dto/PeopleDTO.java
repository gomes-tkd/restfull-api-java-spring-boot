package io.github.gomestdk.rest_with_spring_boot_and_java.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.util.Objects;

@Relation(collectionRelation = "people")
@JsonPropertyOrder({"id", "first_name", "last_name", "gender", "address", "enabled"})
public class PeopleDTO extends RepresentationModel<PeopleDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    private Long id;
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;
    @Column(name = "address", nullable = false, length = 120)
    private String address;
    @Column(name = "gender", nullable = false, length = 6)
    private String gender;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    public PeopleDTO() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PeopleDTO peopleDTO = (PeopleDTO) o;
        return Objects.equals(id, peopleDTO.id) && Objects.equals(firstName, peopleDTO.firstName) && Objects.equals(lastName, peopleDTO.lastName) && Objects.equals(address, peopleDTO.address) && Objects.equals(gender, peopleDTO.gender) && Objects.equals(enabled, peopleDTO.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, firstName, lastName, address, gender, enabled);
    }
}
