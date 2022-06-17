package com.knubookStore.knubookStoreadmin.exception.errors;

import com.knubookStore.knubookStoreadmin.exception.ErrorCode;

public class SellFailedException extends RuntimeException{
    public SellFailedException(){
        super(ErrorCode.SELL_BOOK_FAILED.getMessage());
    }
}
