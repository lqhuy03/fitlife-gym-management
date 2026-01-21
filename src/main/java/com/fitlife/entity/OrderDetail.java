package com.fitlife.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "order_details") // Tên bảng có gạch dưới
public class OrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id") // Sửa thành snake_case
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id") // Sửa thành snake_case
    private Product product;
}