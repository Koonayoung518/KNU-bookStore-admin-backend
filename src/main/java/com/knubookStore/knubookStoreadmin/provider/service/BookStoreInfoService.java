package com.knubookStore.knubookStoreadmin.provider.service;

import com.knubookStore.knubookStoreadmin.entity.BookStoreInfo;
import com.knubookStore.knubookStoreadmin.exception.ErrorCode;
import com.knubookStore.knubookStoreadmin.exception.errors.BookStoreInfoDuplicatedException;
import com.knubookStore.knubookStoreadmin.exception.errors.CustomException;
import com.knubookStore.knubookStoreadmin.exception.errors.NotFoundBookStoreInfoException;
import com.knubookStore.knubookStoreadmin.repository.BookStoreInfoRepository;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBookStoreInfo;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBookStoreInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookStoreInfoService {
    private final BookStoreInfoRepository bookStoreInfoRepository;

    @Transactional
    public void registerBookInfo(RequestBookStoreInfo.RegisterBookStoreInfoDto requestDto) {
        List<BookStoreInfo> bookStoreInfoList = bookStoreInfoRepository.findAll();
        if (bookStoreInfoList.size() > 0) //기본 정보가 이미 있을 경우
        {
            bookStoreInfoList.get(0).updateBookStoreInfo(requestDto.getOperatingTime(),
                    requestDto.getPhone(), requestDto.getLocation(), requestDto.getNotice());
        } else {
            BookStoreInfo bookStoreInfo = BookStoreInfo.builder()
                    .operatingTime(requestDto.getOperatingTime())
                    .phone(requestDto.getPhone())
                    .location(requestDto.getLocation())
                    .notice(requestDto.getNotice())
                    .build();
            bookStoreInfoRepository.save(bookStoreInfo);

        }
    }

    @Transactional(readOnly = true)
    public ResponseBookStoreInfo.BookStoreInfoDto getBookInfo() {
        ResponseBookStoreInfo.BookStoreInfoDto response = null;
        List<BookStoreInfo> bookStoreInfoList = bookStoreInfoRepository.findAll();
        if (bookStoreInfoList.size() != 0) {
            BookStoreInfo bookStoreInfo = bookStoreInfoList.get(0);
            response = ResponseBookStoreInfo.BookStoreInfoDto.builder()
                    .operatingTime(bookStoreInfo.getOperatingTime())
                    .phone(bookStoreInfo.getPhone())
                    .location(bookStoreInfo.getLocation())
                    .notice(bookStoreInfo.getNotice())
                    .build();
        }
        return response;
    }

    @Transactional
    public void deleteBookInfo() {
        List<BookStoreInfo> bookStoreInfoList = bookStoreInfoRepository.findAll();
        if (bookStoreInfoList.size() == 0) {
            throw new CustomException(ErrorCode.NOT_FOUND_BOOKSTORE_INFO);
        }
        BookStoreInfo bookStoreInfo = bookStoreInfoList.get(0);
        bookStoreInfoRepository.delete(bookStoreInfo);
    }
}
