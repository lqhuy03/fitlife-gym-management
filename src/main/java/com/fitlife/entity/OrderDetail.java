package com.fitlife.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "OrderDetails")
public class OrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "OrderId")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;
}