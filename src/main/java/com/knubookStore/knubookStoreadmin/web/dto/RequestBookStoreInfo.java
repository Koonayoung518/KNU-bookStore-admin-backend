package com.knubookStore.knubookStoreadmin.web.dto;

import lombok.Builder;
import lombok.Data;

public class RequestBookStoreInfo {

    @Builder
    @Data
    public static class RegisterBookStoreInfoDto{
        private String operatingTime;
        private String phone;
        private String location;
        private String notice;
    }
}
