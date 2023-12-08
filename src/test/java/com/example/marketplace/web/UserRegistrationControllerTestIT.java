package com.example.marketplace.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationControllerTestIT {

    @Autowired
    private MockMvc mockMvc;
    @Value("${mail.port}")
    private int port;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;
    private GreenMail greenMail;

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(port, host, "smtp"));
        greenMail.start();
        greenMail.setUser(username, password);
    }

    @AfterEach
    void tearDown() {

        greenMail.stop();
    }

    @Test
    void testRegistration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .param("username", "test1234")
                        .param("email", "test1234@test.bg")
                        .param("firstName", "Pesho")
                        .param("lastName", "Petrov")
                        .param("password", "123456")
                        .param("confirmPassword", "123456")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

        greenMail.waitForIncomingEmail(1);
        MimeMessage[] registrationMessages = greenMail.getReceivedMessages();
        assertEquals(1, registrationMessages.length);
        MimeMessage registrationMessage=registrationMessages[0];
        assertTrue(registrationMessage.getContent().toString().contains("test"));
        assertEquals(1,registrationMessage.getAllRecipients().length);
        assertEquals("test1234@test.bg",registrationMessage.getAllRecipients()[0].toString());
    }


}
