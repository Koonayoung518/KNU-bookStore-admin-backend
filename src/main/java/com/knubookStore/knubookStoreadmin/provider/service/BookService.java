package com.knubookStore.knubookStoreadmin.provider.service;

import com.knubookStore.knubookStoreadmin.core.Type.BookType;
import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.exception.ErrorCode;
import com.knubookStore.knubookStoreadmin.exception.CustomException;
import com.knubookStore.knubookStoreadmin.repository.BookRepository;
import com.knubookStore.knubookStoreadmin.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/secret/api-key.properties")
public class BookService {
    private final BookRepository bookRepository;

    @Value("${api.id}")
    private String clientId;
    @Value("${api.secret}")
    private String clientSecret;

    @Transactional
    public ResponseBook.BookDto getBook(String isbn) {
        ResponseBook.BookDto responseBook = null;

        Book book = bookRepository.findByIsbn(isbn);
        if (book != null) { //이미 등록된 책일 경우
            responseBook = ResponseBook.BookDto.of(book);
            return responseBook;
        }
        // 네이버 api로 책 정보 찾기
        try {
            isbn = URLEncoder.encode(isbn, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }
        String url = "https://openapi.naver.com/v1/search/book_adv.xml?d_isbn=" + isbn;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        try {
            org.jsoup.nodes.Document doc = Jsoup.connect(url).headers(requestHeaders).get();
            Iterator<org.jsoup.nodes.Element> rows = doc.select("item").iterator();
            Element item = rows.next();
            String[] isbnList = item.select("isbn").text().split(" ");
            responseBook = ResponseBook.BookDto.builder()
                    .isbn(isbnList[0])
                    .title(item.select("title").text())
                    .publisher(item.select("publisher").text())
                    .author(item.select("author").text().replaceAll("[@&^]", ", "))
                    .price(Integer.parseInt(item.select("discount").text()))
                    .image(item.select("image").text())
                    .pubdate(item.select("pubdate").text())
                    .stock(0)
                    .bookType(BookType.UnRegistered)
                    .build();
        } catch (IOException e) {
            System.out.println("파싱 실패");
        }

        return responseBook;
    }

    @Transactional(readOnly = true)
    public List<ResponseBook.BookListDto> getAllBook() {
        List<ResponseBook.BookListDto> list = new ArrayList<>();
        List<Book> books = bookRepository.findAll();

        books.stream().forEach(book -> list.add(ResponseBook.BookListDto.of(book)));

        return list;
    }

    @Transactional
    public void registerBook(RequestBook.RegisterBookDto requestBook) {
        Book book = bookRepository.findByIsbn(requestBook.getIsbn());
        if (book != null) { //이미 재고가 있는 책일 경우
            throw new CustomException(ErrorCode.BOOK_DUPLICATED);
        }
        book = Book.builder()
                .isbn(requestBook.getIsbn())
                .title(requestBook.getTitle())
                .publisher(requestBook.getPublisher())
                .author(requestBook.getAuthor())
                .price(requestBook.getPrice())
                .image(requestBook.getImage())
                .pubdate(requestBook.getPubdate())
                .stock(requestBook.getStock())
                .type(BookType.Registered)
                .build();
        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(RequestBook.UpdateBookDto updateBook) {
        Book book = bookRepository.findByIsbn(updateBook.getIsbn());
        if (book == null) {//해당하는 책이 없을 경우
            throw new CustomException(ErrorCode.NOT_FOUND_BOOK);
        }
        book.updateBookInfo(updateBook.getPrice(), updateBook.getStock());
    }

    @Transactional
    public void deleteBook(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {// 책이 없을 경우
            throw new CustomException(ErrorCode.NOT_FOUND_BOOK);
        }
        bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public Page<ResponseBook.BookDetailListDto> getAllBookToSite(Pageable pageable) {
        Page<Book> bookList = bookRepository.findAll(pageable);
        return bookList.map(ResponseBook.BookDetailListDto::of);
    }

    @Transactional(readOnly = true)
    public List<ResponseBook.BookListDto> getBooksByTitle(String title) {
        List<ResponseBook.BookListDto> list = new ArrayList<>();
        List<Book> bookList = bookRepository.findByTitleLike(title);

        if (bookList.size() != 0) {
            bookList.stream().forEach(book -> list.add(ResponseBook.BookListDto.of(book)));
        }

        return list;
    }

    @Transactional(readOnly = true)
    public Page<ResponseBook.BookDetailListDto> getBooksByTitleToSite(Pageable pageable, String title) {
        Page<Book> bookList = bookRepository.findByPageOfTitleLike(pageable, title);
        return bookList.map(ResponseBook.BookDetailListDto::of);
    }
}


