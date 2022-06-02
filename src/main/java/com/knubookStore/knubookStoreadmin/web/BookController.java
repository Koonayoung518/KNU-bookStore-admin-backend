package com.knubookStore.knubookStoreadmin.web;

import com.knubookStore.knubookStoreadmin.exception.errors.NotFoundBookException;
import com.knubookStore.knubookStoreadmin.provider.service.BookService;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/knu/book")
    public ResponseEntity<ResponseMessage> getAllBook(@PageableDefault Pageable pageable){
        Page<ResponseBook.getBook> book = bookService.getAllBook(pageable);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("등록된 전체 책 조회 성공")
                .list(book)
                .build(), HttpStatus.OK);
    }
    //책 관리
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

    @PostMapping("/manage/book")
    public ResponseEntity<ResponseMessage> registerBook(@RequestBody RequestBook.registerBook requestDto){
        bookService.registerBook(requestDto);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("책 등록 성공")
                .build(), HttpStatus.OK);
    }
    //판매
    @GetMapping("/sell/book/{isbn}")
    public ResponseEntity<ResponseMessage> getSellBook(@PathVariable(name = "isbn") String isbn){
        ResponseBook.sellBook book = bookService.sellBook(isbn).orElseThrow(()-> new NotFoundBookException());

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("책 조회 성공")
                .list(book)
                .build(), HttpStatus.OK);
    }
    @GetMapping("/dev")
    public String dev(){
        return "Hello world!";
    }
}
