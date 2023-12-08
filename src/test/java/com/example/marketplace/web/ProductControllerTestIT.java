package com.example.marketplace.web;


import com.example.marketplace.model.dtos.UserDTO;
import com.example.marketplace.model.entity.*;
import com.example.marketplace.model.enums.UserRoleEnum;
import com.example.marketplace.repository.*;
import com.example.marketplace.service.ProductService;
import com.example.marketplace.service.UserService;
import com.example.marketplace.testutils.TestDataUtil;
import com.example.marketplace.testutils.UserTestDataUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        testDataUtil.cleanUp();
        userTestDataUtil.cleanUp();
    }

    @AfterEach
    void tearDown() {
        testDataUtil.cleanUp();
        userTestDataUtil.cleanUp();
    }


    @Test
    void testAnonymousDeletionFails() throws Exception {

        UserEntity owner = userTestDataUtil.createUser("test");
        ProductEntity productEntity = testDataUtil.createTestProduct(owner);

        mockMvc.perform(
                        delete("/products/{id}", productEntity.getId())
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/users/login"));
    }

    @Test
    @WithMockUser(username = "manqka123456")
    void testNonAdminUserOwnedOffer() throws Exception {
        UserEntity owner = userTestDataUtil.createUser("manqka123456");
        ProductEntity product = testDataUtil.createTestProduct(owner);

        mockMvc.perform(
                        delete("/products/{id}", product.getId())
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products/all"));
    }


    @WithMockUser(username = "manqka")
    @Test
    void testNonAdminUserNotOwnedOffer() throws Exception {
        UserEntity owner = userTestDataUtil.createUser("manqka123");
        userTestDataUtil.createUser("manqka");
        ProductEntity productEntity = testDataUtil.createTestProduct(owner);

        mockMvc.perform(
                delete("/products/{id}", productEntity.getId())
                        .with(csrf())
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "admincho",
            roles = {"MODERATOR", "ADMIN"})
    void testAdminUserNotOwnedOffer() throws Exception {
        UserEntity owner = userTestDataUtil.createUser("shefa");
        userTestDataUtil.createAdmin("admincho");
        ProductEntity product = testDataUtil.createTestProduct(owner);

        mockMvc.perform(
                        delete("/products/{id}", product.getId())
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products/all"));
    }

    @Test
    @WithMockUser(username = "test")
    void testAddProduct() throws Exception {
        userTestDataUtil.createUser("test");
        mockMvc.perform(post("/products/add")
                        .param("title", "Test Product")
                        .param("description", "Test Description")
                        .param("price", "10.00")
                        .param("primaryImage","test")
                        .param("images","test")
                        .param("subCategory","test")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/all"));
    }


}

