//package com.example.marketplace.testutils;
//
//
//import com.example.marketplace.model.entity.*;
//import com.example.marketplace.model.enums.UserRoleEnum;
//import com.example.marketplace.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.when;
//
//@Component
//public class TestDataUtil {
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private SubCategoryRepository subCategoryRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//
//    public ProductEntity createTestProduct(UserEntity owner){
//        SubCategoryEntity subCategory=subCategoryRepository.save(
//                new SubCategoryEntity().setName("Test subcategory")
//        );
//        CategoryEntity category=categoryRepository.save(
//                new CategoryEntity().setName("Test category")
//        );
//        category.getSubCategories().add(subCategory);
//
//        ProductEntity product=new ProductEntity()
//                .setCategory(category)
//                .setSubCategory(subCategory)
//                .setOwner(owner)
//                .setAddedOn(LocalDateTime.now())
//                .setDescription("test description")
//                .setTitle("test title")
//                .setPrice(BigDecimal.ONE);
//
//        return productRepository.save(product);
//    }

//    public void cleanUp(){
//        productRepository.deleteAll();
//        categoryRepository.deleteAll();;
//        subCategoryRepository.deleteAll();
//    }
//    public UserEntity createTestAdmin() {
//        UserEntity userEntity = new UserEntity().setFirstName("firstName").setLastName("lastName")
//                .setEmail("test@qbvb.bg").setUsername("test").setActive(true)
//                .setRoles(List.of(new UserRoleEntity().setRole(UserRoleEnum.ADMIN),
//                        new UserRoleEntity().setRole(UserRoleEnum.MODERATOR)))
//                .setPassword("123456");
//
//        roleRepository.saveAll(userEntity.getRoles());
//        userRepository.save(userEntity);
//
//
//        return userEntity;
//    }
//    public static UserEntity createTestModerator() {
//        return new UserEntity().setFirstName("firstName").setLastName("lastName")
//                .setEmail("test@qbvb.bg").setUsername("moderator").setActive(true)
//                .setRoles(List.of(new UserRoleEntity().setRole(UserRoleEnum.ADMIN),
//                        new UserRoleEntity().setRole(UserRoleEnum.MODERATOR)))
//                .setPassword("123456");
//    }
//    public UserEntity createTestUser() {
//        UserEntity userEntity = new UserEntity().setFirstName("firstName").setLastName("lastName")
//                .setEmail("test@qbvb.bg").setUsername("user").setActive(true)
//                .setPassword("123456");
//        userRepository.save(userEntity);
//        return userEntity;
//    }
//}
