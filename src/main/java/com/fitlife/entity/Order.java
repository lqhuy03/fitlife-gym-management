package com.fitlife.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "orders") // Chữ thường
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username") // Khóa ngoại trỏ sang Account
    private String username;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date") // Sửa thành snake_case
    private Date createDate = new Date();

    @Column(nullable = false)
    private String address;

    private String status = "PENDING";

    @ManyToOne
    @JoinColumn(name = "username", insertable = false, updatable = false) // Map cùng cột username ở trên
    private Account account;

    @JsonIgnore // QUAN TRỌNG
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
}