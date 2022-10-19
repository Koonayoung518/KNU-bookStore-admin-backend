package com.knubookStore.knubookStoreadmin.provider.service;

import com.knubookStore.knubookStoreadmin.core.Type.PaymentType;
import com.knubookStore.knubookStoreadmin.core.Type.SearchType;
import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.entity.History;
import com.knubookStore.knubookStoreadmin.entity.Sell;
import com.knubookStore.knubookStoreadmin.repository.BookRepository;
import com.knubookStore.knubookStoreadmin.repository.HistoryRepository;
import com.knubookStore.knubookStoreadmin.repository.SellRepository;
import com.knubookStore.knubookStoreadmin.web.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SellServiceTests {
    @Autowired
    SellService sellService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    SellRepository sellRepository;
    @Autowired
    HistoryRepository historyRepository;

    @Test
    @DisplayName("책 판매 내역 저장 테스트")
    @Transactional
    void registerSellBook() {
        Book book = Book.builder()
                .isbn("9788965402602")
                .stock(5)
                .build();
        bookRepository.save(book);
        //책 판매
        List<BookInfo> list = new ArrayList<>();
        BookInfo bookInfo = BookInfo.builder()
                .isbn("9788965402602")
                .amount(2)
                .unitPrice(22000)
                .total(44000)
                .build();
        list.add(bookInfo);
        RequestSell.RegisterSellBookHistoryDto requestDto = RequestSell.RegisterSellBookHistoryDto.builder()
                .bookList(list)
                .totalPrice(64000)
                .payment("Cash")
                .build();
        sellService.sellBook(requestDto);
        Book result = bookRepository.findByIsbn("9788965402602");
        assertEquals(result.getStock(), 3);
    }

    @Test
    @DisplayName("판매내역 전체 조회 테스트(성공)")
    void getAllHistoryTest(){
        History history = History.builder()
                .totalPrice(100000)
                .payment(PaymentType.CASH)
                .build();
        history = historyRepository.save(history);

        Sell sell = Sell.builder()
                .isbn("1234")
                .title("책 제목")
                .unitPrice(23400)
                .amount(3)
                .total(69400)
                .history(history)
                .build();
        sell = sellRepository.save(sell);
        history.addSell(sell);

        History history1 = History.builder()
                .totalPrice(100000)
                .payment(PaymentType.CASH)
                .build();
        history1 = historyRepository.save(history1);
        Sell sell1 = Sell.builder()
                .isbn("1234567")
                .title("책 제목2")
                .unitPrice(23400)
                .amount(3)
                .total(69400)
                .history(history)
                .build();
        sell1 = sellRepository.save(sell1);
        history1.addSell(sell1);

        Page<ResponseSell.HistoryDto> response = sellService.getAllHistory();
        assertEquals(2,response.getTotalElements());
    }

    @Test
    @DisplayName("판매내역 상세 조회 테스트(성공)")
    void getHistoryDetailTest(){
        History history = History.builder()
                .totalPrice(100000)
                .payment(PaymentType.CASH)
                .build();
        history = historyRepository.save(history);

        Sell sell = Sell.builder()
                .isbn("1234")
                .title("책 제목")
                .unitPrice(23400)
                .amount(3)
                .total(69400)
                .history(history)
                .build();
        sell = sellRepository.save(sell);
        history.addSell(sell);

        History history1 = History.builder()
                .totalPrice(100000)
                .payment(PaymentType.CASH)
                .build();
        history1 = historyRepository.save(history1);
        Sell sell1 = Sell.builder()
                .isbn("1234567")
                .title("책 제목2")
                .unitPrice(23400)
                .amount(3)
                .total(69400)
                .history(history)
                .build();
        sell1 = sellRepository.save(sell1);
        history1.addSell(sell1);

        ResponseSell.HistoryDetailDto response = sellService.getHistoryDetail(history.getId());
        assertNotNull(response);
        System.out.println(response.getSellDate());

    }

    @Test
    @DisplayName("판매내역 전체 조건 조회 테스트(성공)")
    void getAllHistoryByConditionTest() {
        History history = History.builder()
                .sellDate(LocalDateTime.now())
                .totalPrice(1000)
                .payment(PaymentType.CASH)
                .build();
        history = historyRepository.save(history);

        Sell sell = Sell.builder()
                .isbn("1234")
                .title("책 제목")
                .unitPrice(23400)
                .amount(3)
                .total(69400)
                .history(history)
                .build();
        sell = sellRepository.save(sell);
        history.addSell(sell);
        System.out.println("history 1 : " + history.getSellDate());
        History history1 = History.builder()
                .sellDate(LocalDateTime.now().minusDays(5))
                .totalPrice(200)
                .payment(PaymentType.CASH)
                .build();
        history1 = historyRepository.save(history1);
        Sell sell1 = Sell.builder()
                .isbn("1234567")
                .title("책 제목2")
                .unitPrice(23400)
                .amount(3)
                .total(69400)
                .history(history)
                .build();
        sell1 = sellRepository.save(sell1);
        history1.addSell(sell1);

        System.out.println("history 2 : " + history1.getSellDate());
        Pageable sortedBySellDateDesc = PageRequest.of(0,10, Sort.by("sellDate").descending());

        //금액 조회
        Page<ResponseSell.HistoryDto> response = sellService.getHistoryByCondition(SearchType.PRICE, LocalDateTime.now(), LocalDateTime.now(), 200,sortedBySellDateDesc);
        assertEquals(1, response.getTotalElements());
        for (ResponseSell.HistoryDto item : response) {
            System.out.println(item.getSellDate() + " " + item.getTotalPrice());
        }
        System.out.println("기간 조회==============");
        //기간 조회
        Page<ResponseSell.HistoryDto> dateResponse = sellService.getHistoryByCondition(SearchType.DATE, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(1), 200,sortedBySellDateDesc);
        assertEquals(1, dateResponse.getTotalElements());
        for (ResponseSell.HistoryDto item : dateResponse) {
        System.out.println(item.getSellDate() + " " + item.getTotalPrice());
    }
       }
}
