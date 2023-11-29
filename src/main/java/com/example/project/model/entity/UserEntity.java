package com.example.project.model.entity;

import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity  extends BaseEntity{


  @Column(nullable = false,name = "user_name")
  private String username;
  @Column(nullable = false)
  private String email;
  @Column(nullable = false)
  private String firstName;
  private String lastName;

  @Column(nullable = false)
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<UserRoleEntity> roles = new ArrayList<>();
  boolean isActive;

  public boolean isActive() {
    return isActive;
  }


  public UserEntity setActive(boolean active) {
    isActive = active;
    return this;
  }

  public UserEntity setEmail(String email) {
    this.email = email;
    return this;
  }



  public UserEntity setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }



  public UserEntity setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }


  public UserEntity setPassword(String password) {
    this.password = password;
    return this;
  }



  public UserEntity setRoles(List<UserRoleEntity> roles) {
    this.roles = roles;
    return this;
  }

  public UserEntity addRole(UserRoleEntity role) {
    this.roles.add(role);
    return this;
  }

  public UserEntity setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPassword() {
    return password;
  }

  public List<UserRoleEntity> getRoles() {
    return roles;
  }
}
