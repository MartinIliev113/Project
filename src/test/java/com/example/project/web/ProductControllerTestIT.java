package com.example.project.web;


import com.example.project.model.entity.ProductEntity;
import com.example.project.model.entity.UserEntity;
import com.example.project.testutils.TestDataUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTestIT {


    @Autowired
    private TestDataUtil testDataUtil;

    @Autowired
    private TestDataUtil userTestDataUtil;

    @Autowired
    private MockMvc mockMvc;

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

        UserEntity owner = testDataUtil.createTestUser();
        ProductEntity productEntity = testDataUtil.createTestProduct(owner);

        mockMvc.perform(
                        delete("/products/{id}", productEntity.getId())
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/users/login"));
    }
    @Test
    @WithMockUser(username = "test")
    void testNonAdminUserOwnedOffer() throws Exception {
        UserEntity owner = userTestDataUtil.createTestUser().setUsername("test");
        ProductEntity productEntity = testDataUtil.createTestProduct(owner);

        mockMvc.perform(
                        delete("/products/{id}", productEntity.getId())
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products/all"));
    }

    @WithMockUser(username = "notOwner")
    @Test
     void testNonAdminUserNotOwnedOffer() throws Exception {
        UserEntity owner = userTestDataUtil.createTestUser().setUsername("owner");
        userTestDataUtil.createTestUser().setUsername("notOwner");
        ProductEntity productEntity = testDataUtil.createTestProduct(owner);

        mockMvc.perform(
                delete("/products/{id}", productEntity.getId())
                        .with(csrf())
        ).andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(
            username = "admin",
            roles = {"USER", "ADMIN"})
    void testAdminUserNotOwnedOffer() throws Exception {
        UserEntity owner = userTestDataUtil.createTestUser();
        userTestDataUtil.createTestAdmin().setUsername("admin");
        ProductEntity productEntity = testDataUtil.createTestProduct(owner);

        mockMvc.perform(
                        delete("/offer/{uuid}", productEntity.getId())
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/offers/all"));
    }

}

