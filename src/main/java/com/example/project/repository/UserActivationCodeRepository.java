package com.example.project.repository;

import com.example.project.model.entity.UserActivationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserActivationCodeRepository extends JpaRepository<UserActivationCodeEntity, Long> {
    Optional<UserActivationCodeEntity> findByActivationCode(String activationCode);
}
