package com.example.project.model.entity;


import com.example.project.model.enums.UserRoleEnum;
import jakarta.persistence.*;


@Entity
@Table(name = "roles")

public class UserRoleEntity extends BaseEntity {

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRoleEnum role;

  public UserRoleEntity setRole(UserRoleEnum role) {
    this.role = role;
    return this;
  }

  public UserRoleEnum getRole() {
    return role;
  }
}
