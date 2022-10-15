package com.knubookStore.knubookStoreadmin.exception.errors;

import com.knubookStore.knubookStoreadmin.exception.ErrorCode;

public class NotFoundHistoryException extends RuntimeException{
    public NotFoundHistoryException(){
        super(ErrorCode.NOT_FOUND_HISTORY.getMessage());
    }
}
