package com.fitlife.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories") // Chữ thường
public class Category implements Serializable {
    @Id
    @Column(columnDefinition = "char(4)")
    private String id;

    @Column(nullable = false)
    private String name;

    @JsonIgnore // QUAN TRỌNG: Ngăn lấy Category lại lôi hết 100 sản phẩm ra
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}