package com.example.marketplace.service;


import com.example.marketplace.model.entity.CategoryEntity;
import com.example.marketplace.model.entity.SubCategoryEntity;
import com.example.marketplace.model.entity.UserEntity;
import com.example.marketplace.model.entity.UserRoleEntity;
import com.example.marketplace.model.enums.UserRoleEnum;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.SubCategoryRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword;
    private final SubCategoryRepository subCategoryRepository;

    public InitService(UserRoleRepository userRoleRepository,
                       UserRepository userRepository,
                       CategoryRepository categoryRepository, PasswordEncoder passwordEncoder,
                       @Value("${app.default.password}") String defaultPassword, SubCategoryRepository subCategoryRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultPassword = defaultPassword;
        this.subCategoryRepository = subCategoryRepository;
    }

    @PostConstruct
    public void init() {
        initRoles();
        initUsers();
        initCategories();
    }

    private void initCategories() {
        if (categoryRepository.count() == 0) {
            var technologyCategory = new CategoryEntity().setName("Technology");
            var clothesCategory = new CategoryEntity().setName("Clothes");
            var vehiclesCategory = new CategoryEntity().setName("Vehicles");
            var carsSubCategory = new SubCategoryEntity().setName("Cars");
            var trucksSubCategory = new SubCategoryEntity().setName("Trucks");
            var motorBikesSubCategory = new SubCategoryEntity().setName("MotorBikes");
            var tShirtSubCategory = new SubCategoryEntity().setName("TShirts");
            var dressesSubCategory = new SubCategoryEntity().setName("Dresses");
            var jacketsSubCategory = new SubCategoryEntity().setName("Jackets");
            var shoesSubCategory = new SubCategoryEntity().setName("Shoes");
            var computersSubCategory = new SubCategoryEntity().setName("Computers");
            var laptopsSubCategory = new SubCategoryEntity().setName("Laptops");
            var camerasSubCategory = new SubCategoryEntity().setName("Cameras");
            technologyCategory.getSubCategories().add(computersSubCategory);
            technologyCategory.getSubCategories().add(laptopsSubCategory);
            technologyCategory.getSubCategories().add(camerasSubCategory);
            clothesCategory.getSubCategories().add(tShirtSubCategory);
            clothesCategory.getSubCategories().add(dressesSubCategory);
            clothesCategory.getSubCategories().add(jacketsSubCategory);
            clothesCategory.getSubCategories().add(shoesSubCategory);
            vehiclesCategory.getSubCategories().add(carsSubCategory);
            vehiclesCategory.getSubCategories().add(trucksSubCategory);
            vehiclesCategory.getSubCategories().add(motorBikesSubCategory);

            subCategoryRepository.saveAll(vehiclesCategory.getSubCategories());
            subCategoryRepository.saveAll(technologyCategory.getSubCategories());
            subCategoryRepository.saveAll(clothesCategory.getSubCategories());
            categoryRepository.save(technologyCategory);
            categoryRepository.save(clothesCategory);
            categoryRepository.save(vehiclesCategory);
        }
    }

    private void initRoles() {
        if (userRoleRepository.count() == 0) {
            var moderatorRole = new UserRoleEntity().setRole(UserRoleEnum.MODERATOR);
            var adminRole = new UserRoleEntity().setRole(UserRoleEnum.ADMIN);

            userRoleRepository.save(moderatorRole);
            userRoleRepository.save(adminRole);
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
            initModerator();
            initNormalUser();
        }
    }

    private void initAdmin() {
        var adminUser = new UserEntity().
                setUsername("Admin").
                setEmail("admin@example.com").
                setFirstName("Admin").
                setLastName("Adminov").
                setPassword(passwordEncoder.encode(defaultPassword)).
                setRoles(userRoleRepository.findAll())
                .setActive(true);

        userRepository.save(adminUser);
    }

    private void initModerator() {

        var moderatorRole = userRoleRepository.
                findUserRoleEntityByRole(UserRoleEnum.MODERATOR).orElseThrow();

        var moderatorUser = new UserEntity().
                setUsername("Moderator").
                setEmail("moderator@example.com").
                setFirstName("Moderator").
                setLastName("Moderatorov").
                setPassword(passwordEncoder.encode(defaultPassword)).
                setRoles(List.of(moderatorRole))
                .setActive(true);

        userRepository.save(moderatorUser);
    }

    private void initNormalUser() {

        var normalUser = new UserEntity().
                setUsername("User").
                setEmail("user@example.com").
                setFirstName("User").
                setLastName("Userov").
                setPassword(passwordEncoder.encode(defaultPassword))
                .setActive(true);

        userRepository.save(normalUser);
    }
}
