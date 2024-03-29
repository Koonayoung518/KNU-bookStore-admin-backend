package com.knubookStore.knubookStoreadmin.web;

import com.knubookStore.knubookStoreadmin.core.Type.SearchType;
import com.knubookStore.knubookStoreadmin.provider.service.SellService;
import com.knubookStore.knubookStoreadmin.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class SellController {
    private final SellService sellService;

    @PostMapping("/admin/sell/book")
    public ResponseEntity<ResponseMessage> registerSellBook(@Valid @RequestBody RequestSell.RegisterSellBookHistoryDto requestDto) {
        sellService.sellBook(requestDto);
        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("책 판매 내역 저장 성공")
                .build());
    }

    @GetMapping("/admin/sell/book/{isbn}")
    public ResponseEntity<ResponseMessage> getSellBook(@PathVariable String isbn) {
        ResponseSell.BookDto book = sellService.getSellBook(isbn);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("판매할 책 정보 조회 성공")
                .data(book)
                .build());
    }

    @GetMapping("/admin/history")
    public ResponseEntity<ResponseMessage> getAllHistory() {
        Page<ResponseSell.HistoryDto> response = sellService.getAllHistory();

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("판매 내역 목록 조회 성공")
                .data(response)
                .build());
    }

    @GetMapping("/admin/history/type")
    public ResponseEntity<ResponseMessage> getHistoryByCondition(@RequestParam("type") String type,
                                                                 @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                 @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                                 @RequestParam("price") Integer price,
                                                                 @PageableDefault(size = 5, sort = "sellDate", direction = Sort.Direction.DESC) Pageable pageable) {
        SearchType searchType = null;
        try {
            searchType = SearchType.valueOf(type);
        } catch (IllegalArgumentException e) {
            searchType = SearchType.DEFAULT;
        }
        Page<ResponseSell.HistoryDto> response = sellService.getHistoryByCondition(searchType, startDate, endDate, price, pageable);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("판매 내역 목록 조건 조회 성공")
                .data(response)
                .build());
    }

    @GetMapping("/admin/history/{id}")
    public ResponseEntity<ResponseMessage> getHistoryDetail(@PathVariable Long id) {
        ResponseSell.HistoryDetailDto response = sellService.getHistoryDetail(id);
        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("판매 내역 상세 조회 성공")
                .data(response)
                .build());
    }

    @GetMapping("/dev")
    public String dev() {
        System.out.println("dev 실행");
        return "dev test 실행";
    }
}
