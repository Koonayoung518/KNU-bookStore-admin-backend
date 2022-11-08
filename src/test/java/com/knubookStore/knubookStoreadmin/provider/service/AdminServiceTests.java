package com.knubookStore.knubookStoreadmin.provider.service;

import com.knubookStore.knubookStoreadmin.repository.AdminRepository;
import com.knubookStore.knubookStoreadmin.web.dto.RequestAdmin;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseAdmin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
public class AdminServiceTests {
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;

    @Test
    @Transactional
    @DisplayName("관리자 회원가입 테스트(성공)")
    public void registerAdminTest(){
        RequestAdmin.Admin request = RequestAdmin.Admin.builder()
                .userId("test")
                .password("1234")
                .build();
        adminService.register(request);
        assertNotNull(adminRepository.findByUserId("test"));
    }


    @Test
    @Transactional
    @DisplayName("관리자 로그인 테스트(성공)")
    public void loginTest(){
        //회원가입
        RequestAdmin.Admin request = RequestAdmin.Admin.builder()
                .userId("test")
                .password("1234")
                .build();
        adminService.register(request);
        //로그인
        ResponseAdmin.Token token = adminService.login(request).get();
        assertNotNull(token);
    }
}
