package com.knubookStore.knubookStoreadmin.entity;

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

    @Column(name = "price")
    private String price; //가격

    @Column(name = "image")
    private String image; //image url

    @Column(name = "input_date")
    private String input_date; //등록 날짜

    @Column(name = "stock")
    private Long stock; //재고
}
