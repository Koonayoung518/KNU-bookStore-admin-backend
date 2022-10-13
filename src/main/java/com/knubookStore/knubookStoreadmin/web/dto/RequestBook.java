package com.knubookStore.knubookStoreadmin.web.dto;

import lombok.Builder;
import lombok.Data;


public class RequestBook {
    @Builder
    @Data
    public static class RegisterBookDto{
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
    public static class UpdateBookDto{
        private String isbn;
        private Integer price;
        private Integer stock;
    }


}
