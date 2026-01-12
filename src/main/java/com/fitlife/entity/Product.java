package com.fitlife.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    private String image;

    @Temporal(TemporalType.DATE)
    @Column(name = "CreateDate")
    private Date createDate = new Date();

    private Boolean available = true;

    @Column(name = "CategoryId")
    private String categoryId; // Lưu ID để tiện truy vấn nhanh nếu không muốn join

    // 'PHYSICAL' hoặc 'MEMBERSHIP'
    @Column(name = "ProductType")
    private String productType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "CategoryId", insertable = false, updatable = false)
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;
}