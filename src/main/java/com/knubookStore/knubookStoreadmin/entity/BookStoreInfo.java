package com.knubookStore.knubookStoreadmin.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "bookstore_info")
@Entity
@Getter
@NoArgsConstructor
public class BookStoreInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation_time")
    private String operatingTime;

    @Column(name = "phone")
    private String phone;

    @Column(name = "location")
    private String location;

    @Column(name = "notice")
    private String notice;

    @Builder
    public BookStoreInfo(String operatingTime, String phone, String location, String notice){
        this.operatingTime = operatingTime;
        this.phone = phone;
        this.location = location;
        this.notice = notice;
    }

    public void updateBookStoreInfo(String operatingTime, String phone, String location, String notice){
        this.operatingTime = operatingTime;
        this.phone = phone;
        this.location = location;
        this.notice = notice;
    }
}
