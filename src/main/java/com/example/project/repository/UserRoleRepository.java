package com.example.project.repository;

import com.example.project.model.entity.UserRoleEntity;
import com.example.project.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
  Optional<UserRoleEntity> findUserRoleEntityByRole(UserRoleEnum role);
}
