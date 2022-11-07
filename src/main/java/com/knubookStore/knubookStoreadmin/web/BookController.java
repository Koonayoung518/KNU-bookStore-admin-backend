package com.knubookStore.knubookStoreadmin.web;

import com.knubookStore.knubookStoreadmin.provider.service.BookService;
import com.knubookStore.knubookStoreadmin.web.dto.RequestBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseBook;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/admin/manage/book")
    public ResponseEntity<ResponseMessage> registerBook(@Valid @RequestBody RequestBook.RegisterBookDto requestDto){
        bookService.registerBook(requestDto);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("책 등록 성공")
                .build());
    }
    @GetMapping("/admin/manage/book")
    public ResponseEntity<ResponseMessage> getAllBook(){
        List<ResponseBook.BookListDto> book = bookService.getAllBook();

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("책 전체 목록 조회 성공")
                .data(book)
                .build());
    }

    @GetMapping("/admin/manage/book/title/{title}")
    public ResponseEntity<ResponseMessage> getBooksByTitle(@PathVariable String title){
        System.out.println("title "+ title);
        List<ResponseBook.BookListDto> list = bookService.getBooksByTitle(title);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("제목 기반 책 목록 조회 성공")
                .data(list)
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
    public ResponseEntity<ResponseMessage> updateBook(@Valid @RequestBody RequestBook.UpdateBookDto requestDto){
        bookService.updateBook(requestDto);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("책 수정 성공")
                .build());
    }


    @GetMapping("/book")
    public ResponseEntity<ResponseMessage> getAllBookToSite(@PageableDefault(size = 10, sort = "registrationDate", direction = Sort.Direction.DESC) Pageable pageable){
        Page<ResponseBook.BookDetailListDto> book = bookService.getAllBookToSite(pageable);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("사이트용 책 조회 성공")
                .data(book)
                .build());
    }

    @GetMapping("/book/title/{title}")
    public ResponseEntity<ResponseMessage> getBooksByTitleToSite(@PageableDefault(size = 10, sort = "registrationDate", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String title){
        Page<ResponseBook.BookDetailListDto> list = bookService.getBooksByTitleToSite(pageable, title);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("사이트용 제목 기반 책 목록 조회 성공")
                .data(list)
                .build());
    }
}
