package com.example.marketplace.web;


import com.example.marketplace.model.AppUserDetails;
import com.example.marketplace.model.dtos.ProductDTO;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

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
    @WithMockUser(username = "test")
    void testNonAdminUserOwnedProduct() throws Exception {
        UserEntity owner = userTestDataUtil.createUser("test");
        ProductEntity product = testDataUtil.createTestProduct(owner);

        mockMvc.perform(
                        delete("/products/{id}", product.getId())
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products/all"));
    }


    @WithMockUser(username = "test")
    @Test
    void testNonAdminUserNotOwnedProduct() throws Exception {
        UserEntity owner = userTestDataUtil.createUser("owner");
        userTestDataUtil.createUser("test");
        ProductEntity productEntity = testDataUtil.createTestProduct(owner);

        mockMvc.perform(
                delete("/products/{id}", productEntity.getId())
                        .with(csrf())
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "test",
            roles = {"MODERATOR", "ADMIN"})
    void testAdminUserNotOwnedProduct() throws Exception {
        UserEntity owner = userTestDataUtil.createUser("owner");
        userTestDataUtil.createAdmin("test");
        ProductEntity product = testDataUtil.createTestProduct(owner);

        mockMvc.perform(
                        delete("/products/{id}", product.getId())
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products/all"));
    }


}

