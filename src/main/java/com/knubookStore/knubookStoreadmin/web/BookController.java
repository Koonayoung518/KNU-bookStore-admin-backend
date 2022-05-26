package com.knubookStore.knubookStoreadmin.web;

import com.knubookStore.knubookStoreadmin.provider.service.BookService;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/admin/book")
    public ResponseEntity<ResponseMessage> getBook(@RequestBody Map<String,String> isbn){
    ResponseBook.getBook book = bookService.getBook(isbn.get("isbn"));

    return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("책 조회 성공")
                .list(book)
                .build(), HttpStatus.OK);
    }
    @PostMapping("/admin/register/book")
    public ResponseEntity<ResponseMessage> registerBook(@RequestBody RequestBook.registerBook requestDto){
        bookService.registerBook(requestDto);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("책 등록 성공")
                .build(), HttpStatus.OK);
    }
}
