package io.github.gomestdk.rest_with_spring_boot_and_java.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.Book;
import jakarta.persistence.Column;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Relation(collectionRelation = "people")
@JsonPropertyOrder({"id", "first_name", "last_name", "gender", "address", "enabled"})
public class PeopleDTO extends RepresentationModel<PeopleDTO> implements Serializable {

    @Serial
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

    @Column(name = "wikipedia_profile_url", length = 255)
    private String profileUrl;

    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    @JsonIgnore
    private List<Book> books;

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

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @JsonIgnore
    public String getName() {
        return (firstName != null ? firstName : "") + (lastName != null ? lastName : "");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PeopleDTO peopleDTO = (PeopleDTO) o;
        return Objects.equals(id, peopleDTO.id) && Objects.equals(firstName, peopleDTO.firstName) && Objects.equals(lastName, peopleDTO.lastName) && Objects.equals(address, peopleDTO.address) && Objects.equals(gender, peopleDTO.gender) && Objects.equals(enabled, peopleDTO.enabled) && Objects.equals(profileUrl, peopleDTO.profileUrl) && Objects.equals(photoUrl, peopleDTO.photoUrl) && Objects.equals(books, peopleDTO.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, firstName, lastName, address, gender, enabled, profileUrl, photoUrl, books);
    }
}
