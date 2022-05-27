package com.knubookStore.knubookStoreadmin.exception.errors;

import com.knubookStore.knubookStoreadmin.exception.ErrorCode;

public class NotFoundBookException extends RuntimeException{
    public NotFoundBookException(){
        super(ErrorCode.NOT_FOUND_BOOK.getMessage());
    }
}
