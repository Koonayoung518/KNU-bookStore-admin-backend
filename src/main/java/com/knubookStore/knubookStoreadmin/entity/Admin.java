package com.knubookStore.knubookStoreadmin.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "admin")
@Entity
@Getter
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name="password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name ="refreshToken")
    private String refreshToken;

    @Builder
    public Admin(String userId, String password, String salt){
        this.userId = userId;
        this.password = password;
        this.salt = salt;
    }
    public void changeRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
