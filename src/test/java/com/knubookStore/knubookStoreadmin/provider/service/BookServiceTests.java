package com.knubookStore.knubookStoreadmin.provider.service;

import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.repository.BookRepository;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class BookServiceTests {
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;


    @Test
    @DisplayName("책 조회(네이버) 테스트")
    @Transactional
    void getBookTest(){
        String isbn = "9788965402602";
        ResponseBook.getBook getBook = bookService.getBook(isbn);
        assertNotNull(getBook);
    }
    @Test
    @DisplayName("책 조회 테스트(이미 등록된 책)")
    @Transactional
    void getBookWhenRegisteredTest(){
        String isbn = "9788965402602";
        Book book = Book.builder()
                .isbn("9788965402602")
                .build();
        bookRepository.save(book);

        ResponseBook.getBook getBook = bookService.getBook(isbn);
        System.out.println(getBook);
    }

    @Test
    @DisplayName("책 전체 조회 테스트(이미 등록된 책)")
    @Transactional
    void getAllBookTest(){
        Book book = Book.builder()
                .isbn("9788965402602")
                .build();
        bookRepository.save(book);
        Book book1 = Book.builder()
                .isbn("9788960777330")
                .build();
        bookRepository.save(book1);
        List<ResponseBook.getBook> getBook = bookService.getAllBook();
        System.out.println(getBook);
    }

    @Test
    @DisplayName("책 삭제 테스트")
    @Transactional
    void deleteBookTest(){
        Book book = Book.builder()
                .isbn("9788965402602")
                .build();
        bookRepository.save(book);
        Book book1 = Book.builder()
                .isbn("9788960777330")
                .build();
        bookRepository.save(book1);
        //페이지 설정
        bookService.deleteBook("9788960777330");
        assertNull(bookRepository.findByIsbn("9788960777330"));
    }
}
