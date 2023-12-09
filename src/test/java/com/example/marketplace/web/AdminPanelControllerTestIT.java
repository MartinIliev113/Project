package com.example.marketplace.web;

import com.example.marketplace.model.dtos.UserDTO;
import com.example.marketplace.model.entity.UserEntity;
import com.example.marketplace.model.exceptions.ObjectNotFoundException;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.service.UserService;
import com.example.marketplace.testutils.DBInit;
import com.example.marketplace.testutils.UserTestDataUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static com.example.marketplace.model.exceptions.ExceptionMessages.USER_NOT_FOUND;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@SpringBootTest
@AutoConfigureMockMvc
public class AdminPanelControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserTestDataUtil userTestDataUtil;
    @Autowired
    UserRepository userRepository;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testAdminEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admins"));
    }
    @Test
    @WithMockUser(username = "nonAdmin")
    public void testAdminEndpointNonAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user" )
    public void testRoleChangerUser() throws Exception {
        String username = "testUser";
        userTestDataUtil.createUser(username);
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/{username}", username)
                        .param("roleName", "MODERATOR"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    @WithMockUser(username = "user")
    public void testDeleteUser() throws Exception {
        String username = "testUser";
        userTestDataUtil.createUser(username);
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/{username}", username))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testDeleteUserAdmin() throws Exception {
        userTestDataUtil.createAdmin("admin");
        String username = "testUser12345";
        UserEntity user = userTestDataUtil.createUser(username);
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/{username}", username).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

}
