package com.example.project.repository;


import com.example.project.model.entity.UserEntity;
import com.example.project.model.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntityByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByRolesContainingAndRolesNotContaining(UserRoleEntity moderator, UserRoleEntity admin);

    List<UserEntity> findByRolesContaining(UserRoleEntity role);

    List<UserEntity> findByRolesNotContaining(UserRoleEntity role);
}
