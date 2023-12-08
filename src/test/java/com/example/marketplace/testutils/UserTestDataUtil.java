package com.example.marketplace.testutils;

import com.example.marketplace.model.entity.UserEntity;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserTestDataUtil {

    @Autowired
    private UserRepository userRepository;


    public UserEntity createUser(String username) {

        UserEntity newUser = new UserEntity()
                .setActive(true)
                .setUsername(username)
                .setEmail("email@example.com")
                .setFirstName("Test user first")
                .setLastName("Test user last")
                .setPassword("123456");

        return userRepository.save(newUser);
    }

    public void cleanUp() {
        userRepository.deleteAll();
    }

}