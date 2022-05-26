package com.knubookStore.knubookStoreadmin.web.dto;

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
    }
}
