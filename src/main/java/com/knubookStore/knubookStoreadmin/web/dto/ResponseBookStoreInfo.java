package com.knubookStore.knubookStoreadmin.web.dto;

import lombok.Builder;
import lombok.Getter;

public class ResponseBookStoreInfo {
    @Builder
    @Getter
    public static class BookStoreInfoDto{
        private String operatingTime;
        private String phone;
        private String location;
        private String notice;
    }
}
