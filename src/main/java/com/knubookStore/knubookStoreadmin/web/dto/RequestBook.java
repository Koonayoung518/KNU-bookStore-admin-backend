package com.knubookStore.knubookStoreadmin.web.dto;

import lombok.Builder;
import lombok.Data;

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
    public static class sellBook{
        private String isbn;
        private String title;
        private Integer price;

    }
}
