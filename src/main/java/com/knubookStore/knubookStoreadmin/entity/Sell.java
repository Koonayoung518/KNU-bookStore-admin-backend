package com.knubookStore.knubookStoreadmin.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "sell")
@Getter
@Entity
@NoArgsConstructor
public class Sell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "unit_price")
    private Integer  unitPrice;//단가

    @Column(name = "amount")
    private Integer amount; //수량

    @Column(name = "price")
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history")
    private History history;

    @Builder
    public Sell(String isbn, Integer unitPrice, Integer amount, Integer price, History history){
        this.isbn = isbn;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.price = price;
        this.history = history;
    }
}
