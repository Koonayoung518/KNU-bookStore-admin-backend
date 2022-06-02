package com.knubookStore.knubookStoreadmin.web;

import com.knubookStore.knubookStoreadmin.exception.errors.NotFoundAdminException;
import com.knubookStore.knubookStoreadmin.provider.security.JwtAuthTokenProvider;
import com.knubookStore.knubookStoreadmin.provider.service.AdminService;
import com.knubookStore.knubookStoreadmin.web.dto.RequestAdmin;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseAdmin;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @PostMapping("/admin/register")
    public ResponseEntity<ResponseMessage> register(@Validated @RequestBody RequestAdmin.Admin requestDto){
        adminService.register(requestDto);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("관리자 등록 성공")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ResponseMessage> login(@Validated @RequestBody RequestAdmin.Admin requestDto){
        ResponseAdmin.Token tokens = adminService.login(requestDto).orElseThrow(()->new NotFoundAdminException());

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("관리자 로그인 성공")
                .list(tokens)
                .build(), HttpStatus.OK);
    }
}
