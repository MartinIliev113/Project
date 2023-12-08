package com.example.marketplace.testutils;

import java.util.List;

import com.example.marketplace.model.entity.UserRoleEntity;
import com.example.marketplace.model.enums.UserRoleEnum;
import com.example.marketplace.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInit implements CommandLineRunner {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRoleRepository.count() == 0) {
            userRoleRepository.saveAll(List.of(
                    new UserRoleEntity().setRole(UserRoleEnum.MODERATOR),
                    new UserRoleEntity().setRole(UserRoleEnum.ADMIN)
            ));
        }
    }
}