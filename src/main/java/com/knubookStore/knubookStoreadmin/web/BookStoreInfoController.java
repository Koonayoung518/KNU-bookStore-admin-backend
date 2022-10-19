package com.knubookStore.knubookStoreadmin.web;

import com.knubookStore.knubookStoreadmin.provider.service.BookStoreInfoService;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBookStoreInfo;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBookStoreInfo;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BookStoreInfoController {
    private final BookStoreInfoService bookStoreInfoService;

    @PostMapping("/admin/bookStoreInfo")
    public ResponseEntity<ResponseMessage> registerBookInfo(@Valid @RequestBody RequestBookStoreInfo.RegisterBookStoreInfoDto requestDto){
        bookStoreInfoService.registerBookInfo(requestDto);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("서점 정보 등록 성공")
                .build());
    }

    @GetMapping("/bookStoreInfo")
    public ResponseEntity<ResponseMessage> getBookInfo(){
        ResponseBookStoreInfo.BookStoreInfoDto response =  bookStoreInfoService.getBookInfo();

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("서점 정보 조회 성공")
                .data(response)
                .build());
    }

    @DeleteMapping("/admin/bookStoreInfo")
    public ResponseEntity<ResponseMessage> deleteBookInfo(){
        bookStoreInfoService.deleteBookInfo();
        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("서점 정보 삭제 성공")
                .build());
    }
}
