package com.knubookStore.knubookStoreadmin.web.dto;

import com.knubookStore.knubookStoreadmin.core.Type.BookType;
import com.knubookStore.knubookStoreadmin.entity.Book;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

public class ResponseBook {
    @Builder
    @Getter
    public static class BookDto{
        private LocalDateTime registrationDate;
        private String isbn;
        private String title;
        private String publisher;
        private String author; //저자
        private Integer price;
        private String image;
        private String pubdate;
        private Integer stock;
        private BookType bookType;

        public static BookDto of(Book book){
            return BookDto.builder()
                    .registrationDate(book.getRegistrationDate())
                    .isbn(book.getIsbn())
                    .title(book.getTitle())
                    .publisher(book.getPublisher())
                    .author(book.getAuthor())
                    .price(book.getPrice())
                    .image(book.getImage())
                    .pubdate(book.getPubdate())
                    .stock(book.getStock())
                    .build();
        }
    }


    @Builder
    @Getter
    public static class BookListDto{
        private String isbn;
        private String title;
        private String publisher;
        private String image;
        private Integer stock;

        public static BookListDto of(Book book){
            return BookListDto.builder()
                    .isbn(book.getIsbn())
                    .title(book.getTitle())
                    .publisher(book.getPublisher())
                    .image(book.getImage())
                    .stock(book.getStock())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class BookDetailListDto{
        private String isbn;
        private String title;
        private String publisher;
        private String author;
        private Integer price;
        private String image;
        private Integer stock;

        public static BookDetailListDto of(Book book){
            return BookDetailListDto.builder()
                    .isbn(book.getIsbn())
                    .title(book.getTitle())
                    .publisher(book.getPublisher())
                    .author(book.getAuthor())
                    .price(book.getPrice())
                    .image(book.getImage())
                    .stock(book.getStock())
                    .build();
        }
    }
}
