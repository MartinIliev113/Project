package com.example.marketplace.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "users_activation_codes")
public class UserForgotPasswordCodeEntity extends BaseEntity {

    private String code;
    private Instant created;
    @ManyToOne
    private UserEntity user;

    public Instant getCreated() {
        return created;
    }

    public UserForgotPasswordCodeEntity setCreated(Instant created) {
        this.created = created;
        return this;
    }

    public String getCode() {
        return code;
    }

    public UserForgotPasswordCodeEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public UserForgotPasswordCodeEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
}
