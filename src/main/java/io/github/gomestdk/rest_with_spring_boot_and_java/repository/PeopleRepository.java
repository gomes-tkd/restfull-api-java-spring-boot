package io.github.gomestdk.rest_with_spring_boot_and_java.repository;

import io.github.gomestdk.rest_with_spring_boot_and_java.model.People;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PeopleRepository extends JpaRepository<People, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE People p SET p.enabled = false WHERE p.id = :id")
    void disablePerson(@Param("id") Long id);

    @Query("SELECT p FROM People p WHERE p.firstName LIKE LOWER(CONCAT ('%',:firstName, '%'))")
    Page<People> findPeopleByName(@Param("firstName") String firstName, Pageable pageable);
}
