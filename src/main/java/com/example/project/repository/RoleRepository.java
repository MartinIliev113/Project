package com.example.project.repository;

import com.example.project.model.entity.UserRoleEntity;
import com.example.project.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<UserRoleEntity,Long> {
    UserRoleEntity findByRole(UserRoleEnum role);
}
