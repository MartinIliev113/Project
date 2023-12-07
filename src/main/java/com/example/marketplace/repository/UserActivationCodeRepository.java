package com.example.marketplace.repository;


import com.example.marketplace.model.entity.UserActivationCodeEntity;
import com.example.marketplace.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserActivationCodeRepository extends JpaRepository<UserActivationCodeEntity, Long> {
    Optional<UserActivationCodeEntity> findByActivationCode(String activationCode);
    Optional<UserActivationCodeEntity> findByUser(UserEntity user);
}
