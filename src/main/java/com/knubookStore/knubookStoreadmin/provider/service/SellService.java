package com.knubookStore.knubookStoreadmin.provider.service;

import com.knubookStore.knubookStoreadmin.core.Type.PaymentType;
import com.knubookStore.knubookStoreadmin.core.Type.SearchType;
import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.entity.History;
import com.knubookStore.knubookStoreadmin.entity.Sell;
import com.knubookStore.knubookStoreadmin.exception.errors.NotFoundBookException;
import com.knubookStore.knubookStoreadmin.exception.errors.NotFoundHistoryException;
import com.knubookStore.knubookStoreadmin.exception.errors.SellFailedException;
import com.knubookStore.knubookStoreadmin.repository.BookRepository;
import com.knubookStore.knubookStoreadmin.repository.HistoryRepository;
import com.knubookStore.knubookStoreadmin.repository.SellRepository;
import com.knubookStore.knubookStoreadmin.web.dto.BookInfo;
import com.knubookStore.knubookStoreadmin.web.dto.RequestSell;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseSell;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellService {
    private final BookRepository bookRepository;
    private final SellRepository sellRepository;
    private final HistoryRepository historyRepository;

    @Transactional
    public Optional<ResponseSell.BookDto> getSellBook(String isbn){
        Book book = bookRepository.findByIsbn(isbn);
        if(book == null){// 책이 없을 경우
            throw new NotFoundBookException();
        }
        ResponseSell.BookDto response = ResponseSell.BookDto.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .unitPrice(book.getPrice())
                .build();
        return Optional.ofNullable(response);
    }

    @Transactional
    public void sellBook(RequestSell.RegisterSellBookHistoryDto requestDto){
        if(requestDto.getBookList().isEmpty()){
            throw new SellFailedException();
        }
        History history = History.builder()
                .sellDate(LocalDateTime.now())
                .totalPrice(requestDto.getTotalPrice())
                .payment(PaymentType.valueOf(requestDto.getPayment()))
                .change(requestDto.getChange())
                .money(requestDto.getMoney())
                .build();
        history = historyRepository.save(history);

        for (BookInfo bookInfo : requestDto.getBookList()){
            Book book = bookRepository.findByIsbn(bookInfo.getIsbn());
            if(book == null){// 책이 없을 경우
                throw new NotFoundBookException();
            }
            //책 수량 변경
            book.updateStock(book.getStock() - bookInfo.getAmount());
            //판매내역 저장
            Sell sell = Sell.builder()
                    .isbn(bookInfo.getIsbn())
                    .title(bookInfo.getTitle())
                    .unitPrice(bookInfo.getUnitPrice())
                    .amount(bookInfo.getAmount())
                    .total(bookInfo.getTotal())
                    .history(history)
                    .build();
            sell = sellRepository.save(sell);
            history.addSell(sell);
        }
    }

    @Transactional(readOnly = true)
    public Page<ResponseSell.HistoryDto> getAllHistory(){
        Pageable sortedBySellDateDesc = PageRequest.of(0,10, Sort.by("sellDate").descending());
        Page<History> historyList = historyRepository.findAll(sortedBySellDateDesc);
        return historyList.map(ResponseSell.HistoryDto::of);
    }

    @Transactional(readOnly = true)
    public ResponseSell.HistoryDetailDto getHistoryDetail(Long id){
        History history = historyRepository.findById(id).orElseThrow(()->new NotFoundHistoryException());
        List<BookInfo> bookInfoList = new ArrayList<>();
        for(Sell item : history.getSellList()){
            BookInfo bookInfo = BookInfo.of(item);
            bookInfoList.add(bookInfo);
        }
        ResponseSell.HistoryDetailDto responseDto = ResponseSell.HistoryDetailDto.builder()
                .sellDate(history.getSellDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                .bookList(bookInfoList)
                .money(history.getMoney())
                .change(history.getChange())
                .totalPrice(history.getTotalPrice())
                .payment(history.getPayment().toString())
                .build();
        return responseDto;
    }

    @Transactional(readOnly = true)
    public Page<ResponseSell.HistoryDto> getHistoryByCondition(String type, LocalDateTime startDate, LocalDateTime endDate,
                                                               Integer price){
        Pageable sortedBySellDateDesc = PageRequest.of(0,10, Sort.by("sellDate").descending());
        Page<History> historyList;
        switch (SearchType.valueOf(type)) {
            case DATE:
                 historyList = historyRepository.findByHistoryByDate(sortedBySellDateDesc, startDate, endDate);

                break;
            case PRICE:
                historyList = historyRepository.findByHistoryByPrice(sortedBySellDateDesc, price);
                break;
            default:
                historyList = historyRepository.findAll(sortedBySellDateDesc);
                break;
        }
        return historyList.map(ResponseSell.HistoryDto::of);
    }
}
