package com.knubookStore.knubookStoreadmin.web.dto;

import com.knubookStore.knubookStoreadmin.entity.History;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

public class ResponseSell {

    @Builder
    @Data
    public static class BookDto{
        private String isbn;
        private String title;
        private Integer unitPrice;
    }

    @Builder
    @Data
    public static class HistoryDto{
        private Long id;
        private Date sellDate;
        private Integer totalPrice;

        public static HistoryDto of(History history){
            return HistoryDto.builder()
                    .id(history.getId())
                    .sellDate(history.getSellDate())
                    .totalPrice(history.getTotalPrice())
                    .build();
        }
    }
}
