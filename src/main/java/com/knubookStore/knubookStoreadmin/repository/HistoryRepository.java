package com.knubookStore.knubookStoreadmin.repository;

import com.knubookStore.knubookStoreadmin.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
//    @Query("select h form History join fetch ")
//    List<History> findAllHistory();
}
