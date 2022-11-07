package com.knubookStore.knubookStoreadmin.repository;

import com.knubookStore.knubookStoreadmin.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByIsbn(String isbn);
    @Query("select b from Book b where b.title like %:title%")
    List<Book>  findByTitleLike(String title);


    @Query(value = "select b from Book b where b.title like %:title%",
            countQuery = "select count(b) from Book b where b.title like %:title%")
    Page<Book>  findByPageOfTitleLike(Pageable pageable, String title);
}
