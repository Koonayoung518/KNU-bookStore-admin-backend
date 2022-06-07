package com.knubookStore.knubookStoreadmin.provider.service;

import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.exception.errors.NotFoundBookException;
import com.knubookStore.knubookStoreadmin.repository.BookRepository;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBook;
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
    public ResponseBook.getBook getBook(String isbn){
        ResponseBook.getBook responseBook =null;

        Book book = bookRepository.findByIsbn(isbn);
        if(book != null){ //이미 등록된 책일 경우
            responseBook = ResponseBook.getBook.builder()
                    .isbn(book.getIsbn())
                    .title(book.getTitle())
                    .publisher(book.getPublisher())
                    .author(book.getAuthor())
                    .price(book.getPrice())
                    .image(book.getImage())
                    .pubdate(book.getPubdate())
                    .stock(book.getStock())
                    .build();
            return responseBook;
        }
        // 네이버 api로 책 정보 찾기
        try {
            isbn = URLEncoder.encode(isbn,"UTF-8");
        }catch (UnsupportedEncodingException e){
            throw new RuntimeException("검색어 인코딩 실패", e);
        }
        String url = "https://openapi.naver.com/v1/search/book_adv.xml?d_isbn="+ isbn;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        try {
            org.jsoup.nodes.Document doc = Jsoup.connect(url).headers(requestHeaders).get();
            Iterator<org.jsoup.nodes.Element> rows = doc.select("item").iterator();
            Element item = rows.next();
            String[] isbnList =  item.select("isbn").text().split(" ");
            responseBook = ResponseBook.getBook.builder()
                    .isbn(isbnList[1])
                    .title(item.select("title").text())
                    .publisher(item.select("publisher").text())
                    .author(item.select("author").text())
                    .price(Integer.parseInt(item.select("price").text()))
                    .image(item.select("image").text())
                    .pubdate(item.select("pubdate").text())
                    .stock(0)
                    .build();
        }catch (IOException e){
            System.out.println("파싱 실패");
        }

        return responseBook;
    }

    @Transactional
    public List<ResponseBook.getBook> getAllBook(){
        List<ResponseBook.getBook> list = new ArrayList<>();
      List<Book> books =  bookRepository.findAll();
      for(Book book : books){
          ResponseBook.getBook responseBook = ResponseBook.getBook.builder()
                  .isbn(book.getIsbn())
                  .title(book.getTitle())
                  .publisher(book.getPublisher())
                  .author(book.getAuthor())
                  .price(book.getPrice())
                  .image(book.getImage())
                  .pubdate(book.getPubdate())
                  .stock(book.getStock())
                  .build();
          list.add(responseBook);
      }
        return list;
    }

    @Transactional
    public void registerBook(RequestBook.registerBook requestBook){
        Book book = bookRepository.findByIsbn(requestBook.getIsbn());
        if(book != null){ //이미 재고가 있는 책일 경우 -입고 시 달라질 가격과 수량만 변경
            book.updateBookInfo(requestBook.getPrice(), requestBook.getStock());
            return;
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
                .build();
        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(String isbn){
        Book book = bookRepository.findByIsbn(isbn);
        if(book == null){// 책이 없을 경우
            throw new NotFoundBookException();
        }
        bookRepository.delete(book);
    }

    //판매
    @Transactional
    public Optional<ResponseBook.sellBook> sellBook(String isbn){
        Book book = bookRepository.findByIsbn(isbn);
        if(book == null){// 책이 없을 경우
            throw new NotFoundBookException();
        }
        ResponseBook.sellBook response = ResponseBook.sellBook.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .price(book.getPrice())
                .build();
        return Optional.ofNullable(response);
    }

}


