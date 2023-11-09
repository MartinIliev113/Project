package com.example.project.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUserDetails extends User {


  private String fullName;
  private String email;
  private Long id;

  public AppUserDetails(String username, String password,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
  }



  public String getFullName() {
    return fullName;
  }

  public AppUserDetails setFullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public AppUserDetails setEmail(String email) {
    this.email = email;
    return this;
  }

  public Long getId() {
    return id;
  }

  public AppUserDetails setId(Long id) {
    this.id = id;
    return this;
  }
}
