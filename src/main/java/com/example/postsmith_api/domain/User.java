package com.example.postsmith_api.domain;

import com.example.postsmith_api.domain.baseEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name = "users")
@Getter
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Setter
    @Column(name = "picture")
    private String picture;

    @Column
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String picture, Role role, String provider) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.provider = provider;
    }
    public String getRoleKey() {
        return this.role.getKey();
    }
    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", picture='" + picture + '\'' +
                ", provider='" + provider + '\'' +
                ", role=" + role +
                '}';
    }
}
