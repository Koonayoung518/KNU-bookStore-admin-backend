package com.knubookStore.knubookStoreadmin.provider.service;

import com.knubookStore.knubookStoreadmin.core.security.role.Role;
import com.knubookStore.knubookStoreadmin.entity.Admin;
import com.knubookStore.knubookStoreadmin.exception.errors.NotFoundAdminException;
import com.knubookStore.knubookStoreadmin.exception.errors.RegisterFailedException;
import com.knubookStore.knubookStoreadmin.provider.security.JwtAuthToken;
import com.knubookStore.knubookStoreadmin.provider.security.JwtAuthTokenProvider;
import com.knubookStore.knubookStoreadmin.repository.AdminRepository;
import com.knubookStore.knubookStoreadmin.util.SHA256Util;
import com.knubookStore.knubookStoreadmin.web.dto.RequestAdmin;
import com.knubookStore.knubookStoreadmin.web.dto.ResponseAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @Transactional
    public void register(RequestAdmin.Admin requestDto){
        Admin admin = adminRepository.findByUserId(requestDto.getUserId());
        if(admin != null){ //아이디 중복
            throw new RegisterFailedException();
        }
        //salt 생성
        String salt = SHA256Util.generateSalt();
        //salt랑 비밀번호 암호화
        String encryptedPassword = SHA256Util.getEncrypt(requestDto.getPassword(),salt);

        admin = Admin.builder()
                .userId(requestDto.getUserId())
                .password(encryptedPassword)
                .salt(salt)
                .build();
        adminRepository.save(admin);
    }
    @Transactional
    public Optional<ResponseAdmin.Token> login(RequestAdmin.Admin requestDto){
        Admin admin = adminRepository.findByUserId(requestDto.getUserId());
        if(admin == null){
            throw new NotFoundAdminException();
        }
        //솔트 꺼내기
        String salt = admin.getSalt();
        admin = adminRepository.findByUserIdAndPassword(requestDto.getUserId(),
                SHA256Util.getEncrypt(requestDto.getPassword(),salt));
        if(admin == null){//비밀번호가 틀렸을 경우
            throw new NotFoundAdminException();
        }
        //로그인 성공
        String refreshToken = createRefreshToken(admin.getUserId());
        ResponseAdmin.Token login = ResponseAdmin.Token.builder()
                .accessToken(createAccessToken(admin.getUserId()))
                .refreshToken(refreshToken)
                .build();
        //refreshToken 업데이트
        admin.changeRefreshToken(refreshToken);

        return Optional.ofNullable(login);
    }
    public String createAccessToken(String userId){
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant());
        JwtAuthToken accessToken = jwtAuthTokenProvider.createAuthToken(userId, Role.ADMIN.getCode(),expiredDate);
        return accessToken.getToken();
    }
    public String createRefreshToken(String userId){
        Date expiredDate = Date.from(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant());
        JwtAuthToken refreshToken = jwtAuthTokenProvider.createAuthToken(userId,Role.ADMIN.getCode(), expiredDate);
        return refreshToken.getToken();
    }
}
