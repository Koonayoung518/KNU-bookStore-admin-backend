package com.knubookStore.knubookStoreadmin.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

public class RequestSell {

    @Builder
    @Data
    public static class RegisterSellBookHistoryDto{
        private List<BookInfo> bookList;
        private Integer totalPrice;
        private Integer change;
        private Integer money;
        private String payment; //결제 수단
    }
}
