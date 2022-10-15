package com.knubookStore.knubookStoreadmin.web.dto;

import com.knubookStore.knubookStoreadmin.entity.History;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
        private LocalDateTime sellDate;
        private Integer totalPrice;

        public static HistoryDto of(History history){
            return HistoryDto.builder()
                    .id(history.getId())
                    .sellDate(history.getSellDate())
                    .totalPrice(history.getTotalPrice())
                    .build();
        }
    }

    @Builder
    @Data
    public static class HistoryDetailDto{
        private String sellDate;
        private List<BookInfo> bookList;
        private Integer totalPrice;
        private Integer money;
        private Integer change;
        private String payment;
    }
}
