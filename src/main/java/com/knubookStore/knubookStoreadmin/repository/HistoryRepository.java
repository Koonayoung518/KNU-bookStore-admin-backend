package com.knubookStore.knubookStoreadmin.repository;

import com.knubookStore.knubookStoreadmin.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query(value = "select distinct h from History h left join h.sellList s where h.totalPrice = :price",
    countQuery = "select count(distinct h) from History h left join h.sellList s where h.totalPrice = :price")
    Page<History> findByHistoryByPrice(Pageable pageable, Integer price);
    @Query("select distinct h from History h left join h.sellList s where h.totalPrice = :price")
    List<History> findListPrice(Integer price);
    @Query(value = "select distinct h from History h left join h.sellList s where h.sellDate between :startDate and :endDate",
    countQuery = "select count(distinct h) from History h left join h.sellList s where h.sellDate between :startDate and :endDate")
    Page<History> findByHistoryByDate(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);
}
