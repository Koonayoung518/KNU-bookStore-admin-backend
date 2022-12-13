package com.knubookStore.knubookStoreadmin.interceptor;

import com.knubookStore.knubookStoreadmin.exception.ErrorCode;
import com.knubookStore.knubookStoreadmin.exception.CustomException;
import com.knubookStore.knubookStoreadmin.provider.security.JwtAuthToken;
import com.knubookStore.knubookStoreadmin.provider.security.JwtAuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        if(token.isPresent()){ //토큰이 있다면
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            if(jwtAuthToken.validate()){ //유효하면
                return true;
            }
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }
        throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
    }
}
