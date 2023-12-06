package com.example.marketplace.repository;


import com.example.marketplace.model.entity.UserForgotPasswordCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserForgotPasswordCodeRepository extends JpaRepository<UserForgotPasswordCodeEntity, Long> {
    Optional<UserForgotPasswordCodeEntity> findByCode(String code);
}
