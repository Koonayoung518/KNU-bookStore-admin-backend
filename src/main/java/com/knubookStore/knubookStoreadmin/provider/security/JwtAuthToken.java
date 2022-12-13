package com.knubookStore.knubookStoreadmin.provider.security;


import com.knubookStore.knubookStoreadmin.core.security.AuthToken;
import com.knubookStore.knubookStoreadmin.exception.ErrorCode;
import com.knubookStore.knubookStoreadmin.exception.CustomException;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
public class JwtAuthToken implements AuthToken<Claims> {
    private final Key key;
    @Getter
    private final String token;

    private static final String AUTHORITIES_KEY = "role";

    JwtAuthToken(String token, Key key) {
        this.key = key;
        this.token = token;
    }

    JwtAuthToken(String id, String role, Date expiredDate, Key key) {
        this.key = key;
        this.token = Jwts.builder()
                .setSubject(id) //토큰 제목 -> 여기서는 어드민 아이디
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredDate)
                .compact();
    }

    @Override
    public boolean validate() {
        return getClaims() != null;
    }

    @Override
    public Claims getClaims() {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token");
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }
}
