package com.knubookStore.knubookStoreadmin.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


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
        @NotEmpty(message = "isbn이 비었습니다.")
        private String isbn;
        @NotNull(message = "가격이 비었습니다.")
        private Integer price;
        @NotNull(message = "재고가 비었습니다.")
        private Integer stock;
    }


}
