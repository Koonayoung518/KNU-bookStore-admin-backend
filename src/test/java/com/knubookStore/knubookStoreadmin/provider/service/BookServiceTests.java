package com.knubookStore.knubookStoreadmin.provider.service;

import com.knubookStore.knubookStoreadmin.core.Type.PaymentType;
import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.entity.History;
import com.knubookStore.knubookStoreadmin.entity.Sell;
import com.knubookStore.knubookStoreadmin.repository.BookRepository;
import com.knubookStore.knubookStoreadmin.repository.HistoryRepository;
import com.knubookStore.knubookStoreadmin.web.dto.BookInfo;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookServiceTests {
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    HistoryRepository historyRepository;

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
    @DisplayName("책 등록 테스트")
    @Transactional
    void registerBookTest(){
        String isbn = "9788965402602";
        RequestBook.registerBook registerBook = RequestBook.registerBook
                .builder()
                .isbn(isbn)
                .build();
        bookService.registerBook(registerBook);
        assertNotNull(bookRepository.findByIsbn(isbn));
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
        //책 삭제
        bookService.deleteBook("9788960777330");
        assertNull(bookRepository.findByIsbn("9788960777330"));
    }

    @Test
    @DisplayName("책 판매 내역 저장 테스트")
    @Transactional
    void registerSellBook() {
        Book book = Book.builder()
                .isbn("9788965402602")
                .stock(5)
                .build();
        bookRepository.save(book);
        //책 판매
        List<BookInfo> list = new ArrayList<>();
        BookInfo bookInfo = BookInfo.builder()
                .isbn("9788965402602")
                .amount(2)
                .unitPrice(22000)
                .price(44000)
                .build();
        list.add(bookInfo);
        RequestBook.sellBook requestDto = RequestBook.sellBook.builder()
                .bookList(list)
                .totalPrice(64000)
                .payment("Cash")
                .build();
        bookService.sellBook(requestDto);
        Book result = bookRepository.findByIsbn("9788965402602");
        assertEquals(result.getStock(), 3);
    }
        }
