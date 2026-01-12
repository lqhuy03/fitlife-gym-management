package com.fitlife.repository;

import com.fitlife.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Tìm theo danh mục
    List<Product> findByCategoryId(String categoryId);

    // Tìm kiếm theo tên (Cho ô Search) - đáp ứng yêu cầu ASM
    List<Product> findByNameContainingIgnoreCase(String name);

    // Lọc theo khoảng giá - đáp ứng yêu cầu ASM
    List<Product> findByPriceBetween(Double min, Double max);

    // Lấy danh sách sản phẩm hiển thị trang chủ (có phân trang)
    Page<Product> findAll(Pageable pageable);
}