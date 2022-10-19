package com.knubookStore.knubookStoreadmin.exception.errors;

import com.knubookStore.knubookStoreadmin.exception.ErrorCode;

public class NotFoundBookStoreInfoException extends RuntimeException{
    public NotFoundBookStoreInfoException(){
        super(ErrorCode.NOT_FOUND_BOOKSTORE_INFO.getMessage());
    }
}
