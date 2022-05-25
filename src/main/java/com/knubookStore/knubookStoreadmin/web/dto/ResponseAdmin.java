package com.knubookStore.knubookStoreadmin.web.dto;

import lombok.Builder;
import lombok.Data;

public class ResponseAdmin {
    @Builder
    @Data
    public static class Token{
        private String accessToken;
        private String refreshToken;
    }
}
