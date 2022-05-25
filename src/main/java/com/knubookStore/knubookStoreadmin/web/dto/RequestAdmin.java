package com.knubookStore.knubookStoreadmin.web.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

public class RequestAdmin {
    @Builder
    @Data
    public static class Admin{
        @NotNull
        private String userId;
        @NotNull
        private String password;
    }
}
