package com.knubookStore.knubookStoreadmin.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RequestSell {

    @Builder
    @Data
    public static class RegisterSellBookHistoryDto{
        private List<BookInfo> bookList;
        @NotNull(message = "총 금액이 비었습니다.")
        private Integer totalPrice;
        private Integer money;
        private Integer change;
        @NotEmpty(message = "결제 수단이 비었습니다.")
        private String payment; //결제 수단
    }
}
