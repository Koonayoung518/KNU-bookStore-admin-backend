package com.knubookStore.knubookStoreadmin.entity;

import com.knubookStore.knubookStoreadmin.core.Type.PaymentType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "history")
@Getter
@Entity
@NoArgsConstructor
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date sellDate = new Date();

    @OneToMany(mappedBy = "history", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Sell> sellList = new ArrayList<>();

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "change")
    private Integer change;

    @Column(name = "money")
    private Integer money;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment")
    private PaymentType payment; //결제 수단

    @Builder
    public History(Integer totalPrice, PaymentType payment, Integer money, Integer change){
        this.totalPrice = totalPrice;
        this.payment = payment;
        this.money = money;
        this.change = change;
    }

    public void addSell(Sell sell){
        this.sellList.add(sell);
    }
}
