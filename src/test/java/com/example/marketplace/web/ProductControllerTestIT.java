package com.example.marketplace.web;


import com.example.marketplace.model.dtos.UserDTO;
import com.example.marketplace.model.entity.*;
import com.example.marketplace.model.enums.UserRoleEnum;
import com.example.marketplace.repository.*;
import com.example.marketplace.service.ProductService;
import com.example.marketplace.service.UserService;
import com.example.marketplace.testutils.TestDataUtil;
import com.example.marketplace.testutils.UserTestDataUtil;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTestIT {


    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TestDataUtil testDataUtil;
    @Autowired
    private UserTestDataUtil userTestDataUtil;




    @Test
    void testAnonymousDeletionFails() throws Exception {

        UserEntity owner = createTestUser();
        ProductEntity productEntity = createTestProduct(owner);

        mockMvc.perform(
                        delete("/products/{id}", productEntity.getId())
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/users/login"));
    }
//    @Test
//    @WithMockUser(username = "test")
//    void testNonAdminUserOwnedOffer() throws Exception {
//        UserEntity owner = createTestUser().setUsername("test");
//        ProductEntity productEntity = createTestProduct(owner);
//
//        mockMvc.perform(
//                        delete("/products/{id}", productEntity.getId())
//                                .with(csrf())
//                ).andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/products/all"));
//    }
@Test
@WithMockUser(username = "manqka12345")
void testNonAdminUserOwnedOffer() throws Exception {
    UserEntity owner = userTestDataUtil.createUser("manqka12345");
    ProductEntity product = testDataUtil.createTestProduct(owner);

    mockMvc.perform(
                    delete("/products/{id}", product.getId())
                            .with(csrf())
            ).andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/products/all"));
}

//    @WithMockUser(username = "notOwner")
//    @Test
//    void testNonAdminUserNotOwnedOffer() throws Exception {
//
//        UserEntity owner = createTestUser().setUsername("owner");
//        UserEntity notOwner = createTestUser().setUsername("notOwner");
//
//
//        ProductEntity productEntity = createTestProduct(owner);
//        UserDTO userDTO=new UserDTO().setUsername(notOwner.getUsername()).setPassword(notOwner.getPassword()).setEmail(notOwner.getEmail());
//
//
//        when(userService.findByUsername("notOwner")).thenReturn(userDTO);
//        when(productService.isOwner(productEntity.getId(), "notOwner")).thenReturn(false);
//
//        mockMvc.perform(
//                        delete("/products/{id}", productEntity.getId())
//                                .with(csrf())
//                )
//                .andExpect(status().isForbidden());
//    }

    @Test
    @WithMockUser(username = "nonOwnerUser")
    void testNonAdminUserNotOwnedOffer() throws Exception {
        UserEntity owner = createTestUser().setUsername("owner");
        UserEntity notOwner = createTestUser().setUsername("notOwner");

        ProductEntity productEntity = createTestProduct(owner);
        productEntity.setOwner(owner);


        when(userService.findByUsername(notOwner.getUsername())).thenReturn(new UserDTO().setUsername(notOwner.getUsername()));
        when(productService.isOwner(productEntity.getId(), notOwner.getUsername())).thenReturn(false);

        mockMvc.perform(
                        delete("/products/{id}", productEntity.getId())
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void testAdminUserNotOwnedOffer() throws Exception { //todo
        UserEntity owner = createTestUser();
        UserEntity admin = createTestAdmin();
        ProductEntity productEntity = createTestProduct(owner);
//        when(productService.isOwner(productEntity.getId(),owner.getUsername())).thenReturn(true);

        mockMvc.perform(
                        delete("/products/{id}", productEntity.getId())
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products/all"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void testAddProduct() throws Exception {
        createTestAdmin();
        mockMvc.perform(post("/products/add")
                        .param("title", "Test Product")
                        .param("description", "Test Description")
                        .param("price", "10.00")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/all"));
    }


    private UserEntity createTestAdmin() {
        UserEntity userEntity = new UserEntity().setFirstName("firstName").setLastName("lastName")
                .setEmail("test@qbvb.bg").setUsername("test").setActive(true)
                .setRoles(List.of(new UserRoleEntity().setRole(UserRoleEnum.ADMIN),
                        new UserRoleEntity().setRole(UserRoleEnum.MODERATOR)))
                .setPassword("123456");

//        roleRepository.saveAll(userEntity.getRoles());
//        userRepository.save(userEntity);


        return userEntity;
    }

    private UserEntity createTestModerator() {
        return new UserEntity().setFirstName("firstName").setLastName("lastName")
                .setEmail("test@qbvb.bg").setUsername("moderator").setActive(true)
                .setRoles(List.of(new UserRoleEntity().setRole(UserRoleEnum.ADMIN),
                        new UserRoleEntity().setRole(UserRoleEnum.MODERATOR)))
                .setPassword("123456");
    }

    private UserEntity createTestUser() {
        UserEntity userEntity = new UserEntity().setFirstName("firstName").setLastName("lastName")
                .setEmail("test@qbvb.bg").setUsername("user").setActive(true)
                .setPassword("123456");
        userRepository.save(userEntity);
        return userEntity;
    }

    public ProductEntity createTestProduct(UserEntity owner) {
        SubCategoryEntity subCategory = subCategoryRepository.save(
                new SubCategoryEntity().setName("Test subcategory")
        );
        CategoryEntity category = categoryRepository.save(
                new CategoryEntity().setName("Test category")
        );
        category.getSubCategories().add(subCategory);

        ProductEntity product = new ProductEntity()
                .setCategory(category)
                .setSubCategory(subCategory)
                .setOwner(owner)
                .setAddedOn(LocalDateTime.now())
                .setDescription("test description")
                .setTitle("test title")
                .setPrice(BigDecimal.ONE);

        return productRepository.save(product);
    }

}

