package com.knubookStore.knubookStoreadmin.web;

import com.knubookStore.knubookStoreadmin.provider.security.JwtAuthTokenProvider;
import com.knubookStore.knubookStoreadmin.provider.service.AdminService;
import com.knubookStore.knubookStoreadmin.web.dto.RequestAdmin;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseAdmin;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ResponseMessage> register(@Validated @RequestBody RequestAdmin.Admin requestDto) {
        adminService.register(requestDto);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("관리자 등록 성공")
                .build());
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ResponseMessage> login(@Validated @RequestBody RequestAdmin.Admin requestDto) {
        ResponseAdmin.Token tokens = adminService.login(requestDto);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("관리자 로그인 성공")
                .data(tokens)
                .build());
    }
}
