package com.example.project.testutils;

import com.example.project.model.entity.*;
import com.example.project.model.enums.UserRoleEnum;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.ProductRepository;
import com.example.project.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TestDataUtil {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public ProductEntity createTestProduct(UserEntity owner){
        SubCategoryEntity subCategory=subCategoryRepository.save(
                new SubCategoryEntity().setName("Test subcategory")
        );
        CategoryEntity category=categoryRepository.save(
                new CategoryEntity().setName("Test category")
        );
        category.getSubCategories().add(subCategory);

        ProductEntity product=new ProductEntity()
                .setCategory(category)
                .setSubCategory(subCategory)
                .setOwner(owner)
                .setAddedOn(LocalDateTime.now())
                .setDescription("test description")
                .setTitle("test title")
                .setPrice(BigDecimal.ONE);

        return productRepository.save(product);
    }

    public void cleanUp(){
        productRepository.deleteAll();
        categoryRepository.deleteAll();;
        subCategoryRepository.deleteAll();
    }
    public static UserEntity createTestAdmin() {
        return new UserEntity().setFirstName("firstName").setLastName("lastName")
                .setEmail("test@qbvb.bg").setUsername("test").setActive(true)
                .setRoles(List.of(new UserRoleEntity().setRole(UserRoleEnum.ADMIN),
                        new UserRoleEntity().setRole(UserRoleEnum.MODERATOR)))
                .setPassword("123456");
    }
    public static UserEntity createTestModerator() {
        return new UserEntity().setFirstName("firstName").setLastName("lastName")
                .setEmail("test@qbvb.bg").setUsername("test").setActive(true)
                .setRoles(List.of(new UserRoleEntity().setRole(UserRoleEnum.ADMIN),
                        new UserRoleEntity().setRole(UserRoleEnum.MODERATOR)))
                .setPassword("123456");
    }
    public static UserEntity createTestUser() {
        return new UserEntity().setFirstName("firstName").setLastName("lastName")
                .setEmail("test@qbvb.bg").setUsername("test").setActive(true)
                .setPassword("123456");
    }
}
