package com.fitlife.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Accounts")
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

    // MySQL: BIT(1) -> Java: Boolean
    private Boolean role = false;
    private Boolean isActive = true;

    @OneToMany(mappedBy = "account")
    private List<Order> orders;
}