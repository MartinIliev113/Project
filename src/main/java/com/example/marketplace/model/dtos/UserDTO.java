package com.example.marketplace.model.dtos;


import com.example.marketplace.model.validation.FieldMatch;
import com.example.marketplace.model.validation.UniqueUserEmail;
import com.example.marketplace.model.validation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@FieldMatch(first = "password", second = "confirmPassword", message = "Passwords should match")
public class UserDTO {

    @Size(min = 3, max = 30)
    @UniqueUsername(message = "Username is taken")
    private String username;
    @Size(min = 3, max = 30)
    private String firstName;
    @Size(min = 3, max = 30)
    private String lastName;
    @Email(message = "Enter valid email")
    @NotBlank(message = "Enter email address")
    @UniqueUserEmail(message = "Email is taken")
    private String email;
    @Size(min = 3, max = 30)
    private String password;
    private String confirmPassword;
    private UserRoleDto role;


    public String getUsername() {
        return username;
    }

    public UserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserDTO setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
