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
@Table(name = "accounts") // Chữ thường
public class Account implements Serializable {
    @Id
    @Column(columnDefinition = "varchar(50)")
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String email;

    private String photo;

    private Boolean activated = true;
    private Boolean admin = false;

    @JsonIgnore // QUAN TRỌNG: Ngăn vòng lặp khi lấy Account -> dính Order -> dính Account
    @OneToMany(mappedBy = "account")
    private List<Order> orders;
}