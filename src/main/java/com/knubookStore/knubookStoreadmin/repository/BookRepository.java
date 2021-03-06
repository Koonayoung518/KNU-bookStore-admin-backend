package com.knubookStore.knubookStoreadmin.repository;

import com.knubookStore.knubookStoreadmin.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByIsbn(String isbn);
}
