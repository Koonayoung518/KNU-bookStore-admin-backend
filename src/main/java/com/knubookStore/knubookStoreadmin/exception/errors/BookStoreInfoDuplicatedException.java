package com.knubookStore.knubookStoreadmin.exception.errors;

import com.knubookStore.knubookStoreadmin.exception.ErrorCode;

public class BookStoreInfoDuplicatedException extends RuntimeException{
    public BookStoreInfoDuplicatedException(){
        super(ErrorCode.BOOKSTORE_INFO_DUPLICATED.getMessage());
    }
}
