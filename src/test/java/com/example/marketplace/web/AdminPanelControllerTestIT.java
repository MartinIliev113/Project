package com.example.marketplace.web;

import com.example.marketplace.model.dtos.UserDTO;
import com.example.marketplace.service.UserService;
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



@SpringBootTest
@AutoConfigureMockMvc
public class AdminPanelControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testAdminEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admins"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testRoleChangeEndpoint() throws Exception {
        String username = "testUser";
        Mockito.when(userService.findByUsername(username)).thenReturn(new UserDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/{username}", username))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("change-role"))
                .andExpect(MockMvcResultMatchers.model().attribute("username", username));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testRoleChangerEndpoint() throws Exception {
        String username = "testUser";
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/{username}", username)
                        .param("roleName", "MODERATOR"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        // You can add additional assertions based on your requirements
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteUserEndpoint() throws Exception {
        String username = "testUser";
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/{username}", username))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        // You can add additional assertions based on your requirements
    }
}
