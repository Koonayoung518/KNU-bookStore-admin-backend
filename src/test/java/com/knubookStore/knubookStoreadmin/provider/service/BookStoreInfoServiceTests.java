package com.knubookStore.knubookStoreadmin.provider.service;

import com.knubookStore.knubookStoreadmin.entity.Book;
import com.knubookStore.knubookStoreadmin.entity.BookStoreInfo;
import com.knubookStore.knubookStoreadmin.exception.errors.NotFoundBookStoreInfoException;
import com.knubookStore.knubookStoreadmin.repository.BookStoreInfoRepository;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBookStoreInfo;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBookStoreInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class BookStoreInfoServiceTests {
    @Autowired
    BookStoreInfoService bookStoreInfoService;
    @Autowired
    BookStoreInfoRepository bookStoreInfoRepository;

    @Test
    @Transactional
    @DisplayName("정보 등록 테스트(성공)")
    void registerBookStoreInfoTest(){
        RequestBookStoreInfo.RegisterBookStoreInfoDto request = RequestBookStoreInfo.RegisterBookStoreInfoDto.builder()
                .operatingTime("9:00-17:00")
                .location("인사관")
                .phone("031280~~")
                .notice("내일 휴무입니다.")
                .build();
        bookStoreInfoService.registerBookInfo(request);
        List<BookStoreInfo> bookStoreInfo = bookStoreInfoRepository.findAll();
        assertEquals(1, bookStoreInfo.size());

    }

    @Test
    @Transactional
    @DisplayName("정보 등록 테스트(성공 - 이미 정보가 있을 경우)")
    void registerBookStoreInfoTestWhenExistInfo(){
        BookStoreInfo register = BookStoreInfo.builder()
                .operatingTime("9:00-17:00")
                .location("인사관")
                .phone("031280~~")
                .notice("내일 휴무입니다.")
                .build();
        register = bookStoreInfoRepository.save(register);
        //재 등록
        RequestBookStoreInfo.RegisterBookStoreInfoDto request = RequestBookStoreInfo.RegisterBookStoreInfoDto.builder()
                .operatingTime("9:00-17:00")
                .location("바뀐 위치")
                .phone("031280~~")
                .notice("내일 휴무입니다.")
                .build();
        bookStoreInfoService.registerBookInfo(request);
        assertEquals(1, bookStoreInfoRepository.findAll().size());
        assertTrue(bookStoreInfoRepository.findAll().contains(register));
    }

    @Test
    @Transactional
    @DisplayName("정보 등록 테스트(성공 - 공지 없을 경우)")
    void registerBookStoreInfoTestWhenNotNotice(){
        RequestBookStoreInfo.RegisterBookStoreInfoDto request = RequestBookStoreInfo.RegisterBookStoreInfoDto.builder()
                .operatingTime("9:00-17:00")
                .location("바뀐 위치")
                .phone("031280~~")
                .build();
        bookStoreInfoService.registerBookInfo(request);

        List<BookStoreInfo> bookStoreInfo = bookStoreInfoRepository.findAll();
        assertEquals(1, bookStoreInfo.size());
    }

    @Test
    @Transactional
    @DisplayName("정보 조회 테스트(성공)")
    void getBookStoreInfoTest(){
        BookStoreInfo register = BookStoreInfo.builder()
                .operatingTime("9:00-17:00")
                .location("인사관")
                .phone("031280~~")
                .notice("내일 휴무입니다.")
                .build();
        bookStoreInfoRepository.save(register);
        ResponseBookStoreInfo.BookStoreInfoDto response = bookStoreInfoService.getBookInfo();
        assertNotNull(response);
        }

    @Test
    @Transactional
    @DisplayName("정보 삭제 테스트(성공)")
    void deleteBookStoreInfoTest(){
        BookStoreInfo register = BookStoreInfo.builder()
                .operatingTime("9:00-17:00")
                .location("인사관")
                .phone("031280~~")
                .notice("내일 휴무입니다.")
                .build();
        bookStoreInfoRepository.save(register);
        bookStoreInfoService.deleteBookInfo();
        assertEquals(0, bookStoreInfoRepository.findAll().size());
        }
}
