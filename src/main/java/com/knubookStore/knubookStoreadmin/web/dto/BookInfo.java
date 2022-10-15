package com.knubookStore.knubookStoreadmin.web.dto;

import com.knubookStore.knubookStoreadmin.entity.Sell;
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
    public static BookInfo of(Sell sell){
        return BookInfo.builder()
                .isbn(sell.getIsbn())
                .title(sell.getTitle())
                .unitPrice(sell.getUnitPrice())
                .amount(sell.getAmount())
                .total(sell.getTotal())
                .build();
    }
}
