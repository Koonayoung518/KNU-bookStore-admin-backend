package com.knubookStore.knubookStoreadmin.exception.errors;

import com.knubookStore.knubookStoreadmin.exception.ErrorCode;

public class NotFoundAdminException extends RuntimeException{
    public NotFoundAdminException(){
        super(ErrorCode.NOT_FOUND_ADMIN.getMessage());
    }
}
