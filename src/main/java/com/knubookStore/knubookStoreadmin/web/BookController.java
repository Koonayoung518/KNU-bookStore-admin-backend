package com.knubookStore.knubookStoreadmin.web;

import com.knubookStore.knubookStoreadmin.exception.errors.NotFoundBookException;
import com.knubookStore.knubookStoreadmin.provider.service.BookService;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    //책 관리
    @GetMapping("/manage/book")
    public ResponseEntity<ResponseMessage> getAllBook(){
        List<ResponseBook.getBook> book = bookService.getAllBook();
        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("등록된 책 전체 조회 성공")
                .list(book)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/manage/book/{isbn}")
    public ResponseEntity<ResponseMessage> getBook(@PathVariable(name = "isbn") String isbn){
    ResponseBook.getBook book = bookService.getBook(isbn);

    return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("책 조회 성공")
                .list(book)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/manage/book/{isbn}")
    public ResponseEntity<ResponseMessage> deleteBook(@PathVariable(name = "isbn") String isbn){
        bookService.deleteBook(isbn);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("책 삭제 성공")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/manage/register/book")
    public ResponseEntity<ResponseMessage> registerBook(@RequestBody RequestBook.registerBook requestDto){
        bookService.registerBook(requestDto);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("책 등록 성공")
                .build(), HttpStatus.OK);
    }
    @PostMapping("/manage/update/book")
    public ResponseEntity<ResponseMessage> updateBook(@RequestBody RequestBook.updateBook requestDto){
        bookService.updateBook(requestDto);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("책 수정 성공")
                .build(), HttpStatus.OK);
    }
    //판매
    @GetMapping("/sell/book/{isbn}")
    public ResponseEntity<ResponseMessage> getSellBook(@PathVariable(name = "isbn") String isbn){
        ResponseBook.sellBook book = bookService.sellBook(isbn).orElseThrow(()-> new NotFoundBookException());

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("판매할 책 조회 성공")
                .list(book)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/dev")
    public String dev(){
        System.out.println("dev 실행");
        return "Hello world!";
    }
}
