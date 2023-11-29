package com.example.project.repository;

import com.example.project.model.entity.UserForgotPasswordCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserForgotPasswordCodeRepository extends JpaRepository<UserForgotPasswordCodeEntity,Long> {
    Optional<UserForgotPasswordCodeEntity> findByCode(String code);
}
