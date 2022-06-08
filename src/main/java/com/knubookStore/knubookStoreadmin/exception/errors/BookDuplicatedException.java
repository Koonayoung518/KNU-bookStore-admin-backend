package com.knubookStore.knubookStoreadmin.exception.errors;

import com.knubookStore.knubookStoreadmin.exception.ErrorCode;

public class BookDuplicatedException extends RuntimeException{
    public BookDuplicatedException(){
        super(ErrorCode.BOOK_DUPLICATED.getMessage());
    }
}
