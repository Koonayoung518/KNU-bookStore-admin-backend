package com.knubookStore.knubookStoreadmin.web.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class RequestBook {
    @Builder
    @Data
    public static class registerBook{
        private String isbn;
        private String title;
        private String publisher;
        private String author; //저자
        private Integer price;
        private String image;
        private String pubdate;
        private Integer stock;
    }

    @Builder
    @Data
    public static class updateBook{
        private String isbn;
        private Integer price;
        private Integer stock;
    }

    @Builder
    @Data
    public static class sellBook{
        private List<BookInfo> bookList;
        private Integer totalPrice;
        private String payment; //결제 수단
    }
}
