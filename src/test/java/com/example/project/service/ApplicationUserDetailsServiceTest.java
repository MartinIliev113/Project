package com.example.project.service;


import com.example.project.model.entity.UserEntity;
import com.example.project.model.enums.UserRoleEnum;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.example.project.testutils.TestDataUtil.createTestAdmin;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationUserDetailsServiceTest {

    private ApplicationUserDetailsService serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        serviceToTest = new ApplicationUserDetailsService(
                mockUserRepository
        );
    }

    @Test
    void testUserNotFound() {
        assertThrows(
                UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("pesho@softuni.bg")
        );
    }

    @Test
    void testUserFoundException() {
        UserEntity testUserEntity = createTestAdmin();
        when(mockUserRepository.findByUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        UserDetails userDetails = serviceToTest.loadUserByUsername(testUserEntity.getUsername());

        assertNotNull(userDetails);
        assertEquals(testUserEntity.getUsername(), userDetails.getUsername());
        assertEquals(testUserEntity.getPassword(), userDetails.getPassword());
        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(containsAuthority(userDetails, "ROLE_"+UserRoleEnum.ADMIN));
        assertTrue(containsAuthority(userDetails, "ROLE_"+UserRoleEnum.MODERATOR));

    }

    private boolean containsAuthority(UserDetails userDetails, String authority) {
        return userDetails.getAuthorities().stream()
                .anyMatch(a -> authority.equals(a.getAuthority()));
    }


}