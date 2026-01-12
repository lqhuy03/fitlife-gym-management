package com.fitlife.repository;

import com.fitlife.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Lịch sử mua hàng của user (Mới nhất lên đầu)
    List<Order> findByUsernameOrderByCreateDateDesc(String username);
}