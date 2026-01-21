package com.fitlife.repository;

import com.fitlife.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Khai báo phương thức để lọc sản phẩm theo mã danh mục
    // Spring Data JPA sẽ tự hiểu và tạo lệnh: SELECT * FROM products WHERE category_id = ?
    List<Product> findByCategoryId(String cid);

    // Bạn có thể thêm phương thức tìm kiếm tên nếu cần cho giao diện
    List<Product> findByNameContainingIgnoreCase(String name);
}