package com.project.stocker.entity;


import com.project.stocker.util.Auditing;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false, unique = true)
    private String email;

    private boolean status;

    @OneToMany(mappedBy = "user")
    private List<Buy> buys = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Sell> sells = new ArrayList<>();

    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = true;
    }

    public void disabled() {
        this.status = false;
    }
}
