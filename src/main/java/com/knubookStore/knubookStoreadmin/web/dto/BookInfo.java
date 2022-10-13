package com.knubookStore.knubookStoreadmin.web.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BookInfo {
    private String isbn;
    private String title;
    private Integer unitPrice;
    private Integer amount; //수량
    private Integer total;
}
