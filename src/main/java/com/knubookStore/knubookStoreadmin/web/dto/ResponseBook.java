package com.knubookStore.knubookStoreadmin.web.dto;

import com.knubookStore.knubookStoreadmin.entity.Book;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;

public class ResponseBook {
    @Builder
    @Data
    public static class getBook{
        private String isbn;
        private String title;
        private String publisher;
        private String author; //저자
        private Integer price;
        private String image;
        private String pubdate;
        private Integer stock;

        public static getBook of(Book book){
            return getBook.builder()
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
    @Data
    public static class sellBook{
        private String isbn;
        private String title;
        private Integer price;
    }
}
