package com.knubookStore.knubookStoreadmin.repository;

import com.knubookStore.knubookStoreadmin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findByUserId(String userId);
    Admin findByUserIdAndPassword(String userId, String password);
}
