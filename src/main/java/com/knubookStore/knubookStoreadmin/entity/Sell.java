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

    @Column(name = "title")
    private String title;

    @Column(name = "unit_price")
    private Integer  unitPrice;//단가

    @Column(name = "amount")
    private Integer amount; //수량

    @Column(name = "total")
    private Integer total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history")
    private History history;

    @Builder
    public Sell(String isbn, String title, Integer unitPrice, Integer amount, Integer total, History history){
        this.isbn = isbn;
        this.title = title;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.total = total;
        this.history = history;
    }
}
