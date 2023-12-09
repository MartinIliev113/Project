package com.example.marketplace.web;


import com.example.marketplace.model.entity.CommentEntity;
import com.example.marketplace.model.entity.ProductEntity;
import com.example.marketplace.model.entity.UserEntity;
import com.example.marketplace.testutils.TestDataUtil;
import com.example.marketplace.testutils.UserTestDataUtil;
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
class CommentControllerTestIT {

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

        UserEntity author = userTestDataUtil.createUser("author");
        ProductEntity productEntity = testDataUtil.createTestProduct(author);
        CommentEntity testComment = testDataUtil.createTestComment(author);
        testDataUtil.addAuthor(testComment,author);
        String url="/comments/"+productEntity.getId()+"/"+testComment.getId();

        mockMvc.perform(
                        delete(url)
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/users/login"));
    }
    @Test
    @WithMockUser(username = "test")
    void testNonAdminUserOwnedComment() throws Exception {
        UserEntity user = userTestDataUtil.createUser("test");
        ProductEntity productEntity = testDataUtil.createTestProduct(user);
        CommentEntity testComment = testDataUtil.createTestComment(user);
        testDataUtil.addAuthor(testComment,user);
        testDataUtil.addProductToComment(testComment,productEntity);
        String url="/comments/"+productEntity.getId()+"/"+testComment.getId();
        mockMvc.perform(
                        delete(url)
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products/details/"+productEntity.getId()));
    }
    @WithMockUser(username = "test")
    @Test
    void testNonAdminUserNotOwnedComment() throws Exception {
        UserEntity author = userTestDataUtil.createUser("author");
        userTestDataUtil.createUser("test");
        ProductEntity productEntity = testDataUtil.createTestProduct(author);
        CommentEntity testComment = testDataUtil.createTestComment(author);
        testDataUtil.addAuthor(testComment,author);
        testDataUtil.addProductToComment(testComment,productEntity);
        String url="/comments/"+productEntity.getId()+"/"+testComment.getId();

        mockMvc.perform(
                delete(url)
                        .with(csrf())
        ).andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(
            username = "test",
            roles = {"MODERATOR", "ADMIN"})
    void testAdminUserNotOwnedOffer() throws Exception {
        UserEntity author = userTestDataUtil.createUser("author");
        userTestDataUtil.createAdmin("test");
        ProductEntity product = testDataUtil.createTestProduct(author);
        CommentEntity testComment = testDataUtil.createTestComment(author);
        testDataUtil.addAuthor(testComment,author);
        testDataUtil.addProductToComment(testComment,product);
        String url="/comments/"+product.getId()+"/"+testComment.getId();

        mockMvc.perform(
                        delete(url)
                                .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products/details/"+product.getId()));
    }


}