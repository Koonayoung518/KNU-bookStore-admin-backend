package com.knubookStore.knubookStoreadmin.provider.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.repository.BookRepository;
import com.knubookStore.knubookStoreadmin.repository.HistoryRepository;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class BookServiceTests {
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("책 조회(네이버) 테스트")
    @Transactional
    void getBookTest(){
        String isbn = "9788965402602";
        ResponseBook.BookDto getBook = bookService.getBook(isbn);
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

        ResponseBook.BookDto getBook = bookService.getBook(isbn);
        assertNotNull(getBook);
        assertNotNull(getBook.getRegistrationDate());
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
        List<ResponseBook.BookListDto> getBook = bookService.getAllBook();
        System.out.println(getBook);
    }
    @Test
    @DisplayName("책 등록 테스트")
    @Transactional
    void registerBookTest(){
        String isbn = "9788965402602";
        RequestBook.RegisterBookDto registerBook = RequestBook.RegisterBookDto
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
    @DisplayName("책 제목으로 목록 조회 테스트")
    @Transactional
    void getBooksByTitle(){
        Book book = Book.builder()
                .isbn("9788965402602")
                .title("가나다")
                .build();
        bookRepository.save(book);

        Book book1 = Book.builder()
                .isbn("9788965402622")
                .title("라가나")
                .build();
        bookRepository.save(book1);


        Book book2 = Book.builder()
                .isbn("9788965402622")
                .title("마바사")
                .build();
        bookRepository.save(book2);

        List<ResponseBook.BookListDto> list = bookService.getBooksByTitle("가나");
        assertEquals(2, list.size());

        for(ResponseBook.BookListDto bookListDto :list){
            System.out.println(bookListDto.getTitle());
        }
    }
        }
