package com.knubookStore.knubookStoreadmin.web;

import com.knubookStore.knubookStoreadmin.provider.service.BookService;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/admin/manage/book")
    public ResponseEntity<ResponseMessage> registerBook(@RequestBody RequestBook.RegisterBookDto requestDto){
        bookService.registerBook(requestDto);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("책 등록 성공")
                .build());
    }
    @GetMapping("/admin/manage/book")
    public ResponseEntity<ResponseMessage> getAllBook(){
        List<ResponseBook.BookListDto> book = bookService.getAllBook();

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("책 전체 조회 성공")
                .data(book)
                .build());
    }

    @GetMapping("/admin/manage/book/{isbn}")
    public ResponseEntity<ResponseMessage> getBook(@PathVariable String isbn){
        ResponseBook.BookDto book = bookService.getBook(isbn);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("책 조회 성공")
                .data(book)
                .build());
    }

    @DeleteMapping("/admin/manage/book/{isbn}")
    public ResponseEntity<ResponseMessage> deleteBook(@PathVariable String isbn){
        bookService.deleteBook(isbn);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("책 삭제 성공")
                .build());

    }

    @PutMapping("/admin/manage/book")
    public ResponseEntity<ResponseMessage> updateBook(@RequestBody RequestBook.UpdateBookDto requestDto){
        bookService.updateBook(requestDto);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("책 수정 성공")
                .build());
    }

}
