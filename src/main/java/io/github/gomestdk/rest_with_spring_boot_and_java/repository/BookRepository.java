package io.github.gomestdk.rest_with_spring_boot_and_java.repository;

import io.github.gomestdk.rest_with_spring_boot_and_java.model.Book;
import io.github.gomestdk.rest_with_spring_boot_and_java.model.People;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
//    @Query("SELECT " +
//            "b.id, b.author, b.launch_date AS  launchDate, b.price, b.title" +
//            "FROM Book b" +
//            "JOIN people_books pb" +
//            "ON pb.book_id = b.id" +
//            "WHERE pb.people_id = 1"
//    )
//    Page<Book> findBookByName(@Param("title") String title, Pageable pageable);
}
