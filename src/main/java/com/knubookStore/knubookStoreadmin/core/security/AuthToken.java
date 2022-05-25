package com.knubookStore.knubookStoreadmin.core.security;

public interface AuthToken<T> {
    boolean validate();
    T getClaims();
}
