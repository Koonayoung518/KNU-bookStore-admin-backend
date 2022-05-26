package com.knubookStore.knubookStoreadmin.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "book")
@Entity
@Getter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isbn")
    private String isbn; //isbn 번호

    @Column(name = "title")
    private String title; //표제

    @Column(name = "publisher")
    private String publisher; //출판사

    @Column(name = "author")
    private String author; //저자

    @Column(name = "price")
    private Integer price; //가격

    @Column(name = "image")
    private String image; //image url

    @Column(name = "pubDate")
    private String pubdate; //출간일

    @Column(name = "stock")
    private Integer stock; //재고

    @Builder
    public Book(String isbn, String title, String publisher, String author, Integer price, String image,
                String pubdate, Integer stock){
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.author = author;
        this.price = price;
        this.image = image;
        this.pubdate = pubdate;
        this.stock =stock;

    }
}
