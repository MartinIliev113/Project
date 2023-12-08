package com.example.marketplace.service;

import com.example.marketplace.model.dtos.UserDTO;
import com.example.marketplace.model.dtos.UserRoleDto;
import com.example.marketplace.model.entity.UserEntity;
import com.example.marketplace.model.entity.UserRoleEntity;
import com.example.marketplace.model.enums.UserRoleEnum;
import com.example.marketplace.model.exceptions.ObjectNotFoundException;
import com.example.marketplace.repository.RoleRepository;
import com.example.marketplace.repository.UserActivationCodeRepository;
import com.example.marketplace.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    //ALL WORKING

    private UserService serviceToTest;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private UserActivationCodeRepository userActivationCodeRepository;
    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        serviceToTest = new UserService(userRepository, passwordEncoder, applicationEventPublisher, modelMapper, roleRepository, userActivationCodeRepository, userDetailsService);
    }

    @Test
    void getModerators() {
        List<UserEntity> moderators = new ArrayList<>();
        moderators.add(createTestModerator());
        when(roleRepository.findByRole(UserRoleEnum.MODERATOR)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.MODERATOR));
        when(roleRepository.findByRole(UserRoleEnum.ADMIN)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.ADMIN));
        when(userRepository.findByRolesContainingAndRolesNotContaining(roleRepository.findByRole(UserRoleEnum.MODERATOR),
                roleRepository.findByRole(UserRoleEnum.ADMIN))).thenReturn(moderators);

        List<UserDTO> moderatorsFound = serviceToTest.getModerators();

        assertEquals(1, moderatorsFound.size());
    }

    @Test
    void getModeratorsEmpty() {
        when(roleRepository.findByRole(UserRoleEnum.MODERATOR)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.MODERATOR));
        when(roleRepository.findByRole(UserRoleEnum.ADMIN)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.ADMIN));
        when(userRepository.findByRolesContainingAndRolesNotContaining(roleRepository.findByRole(UserRoleEnum.MODERATOR),
                roleRepository.findByRole(UserRoleEnum.ADMIN))).thenReturn(new ArrayList<>());

        List<UserDTO> moderatorsFound = serviceToTest.getModerators();

        assertEquals(0, moderatorsFound.size());
    }

    @Test
    void getAdmins() {
        List<UserEntity> admins = new ArrayList<>();
        admins.add(createTestAdmin());
        when(roleRepository.findByRole(UserRoleEnum.ADMIN)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.ADMIN));
        when(userRepository.findByRolesContaining(roleRepository.findByRole(UserRoleEnum.ADMIN))).thenReturn(admins);

        List<UserDTO> adminsFound = serviceToTest.getAdmins();

        assertEquals(1, adminsFound.size());
    }

    @Test
    void getAdminsEmpty() {
        when(roleRepository.findByRole(UserRoleEnum.ADMIN)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.ADMIN));
        when(userRepository.findByRolesContaining(roleRepository.findByRole(UserRoleEnum.ADMIN))).thenReturn(new ArrayList<>());

        List<UserDTO> adminsFound = serviceToTest.getAdmins();

        assertEquals(0, adminsFound.size());
    }

    @Test
    void getUsers() {
        List<UserEntity> users = new ArrayList<>();
        users.add(createTestUser());
        when(roleRepository.findByRole(UserRoleEnum.MODERATOR)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.MODERATOR));
        when(userRepository.findByRolesNotContaining(roleRepository.findByRole(UserRoleEnum.MODERATOR))).thenReturn(users);

        List<UserDTO> usersFound = serviceToTest.getUsers();

        assertEquals(1, usersFound.size());
    }

    @Test
    void getUsersEmpty() {

        when(roleRepository.findByRole(UserRoleEnum.MODERATOR)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.MODERATOR));
        when(userRepository.findByRolesNotContaining(roleRepository.findByRole(UserRoleEnum.MODERATOR))).thenReturn(new ArrayList<>());

        List<UserDTO> usersFound = serviceToTest.getUsers();

        assertEquals(0, usersFound.size());
    }

    @Test
    void findByUsernameUserExists() {
        UserEntity userEntity = createTestUser();
        when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
        UserDTO expectedUserDTO = new UserDTO();
        when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(expectedUserDTO);
        UserDTO actualUserDTO = serviceToTest.findByUsername(userEntity.getUsername());

        assertEquals(expectedUserDTO, actualUserDTO);
    }

    @Test
    void findByUsernameUserNotExists() {
        String nonExistentUsername = "nonexistentuser";

        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> serviceToTest.findByUsername(nonExistentUsername));
    }

    @Test
    void changeRoleFromUserToAdmin() {
        UserEntity testUser = createTestUser();
        UserRoleDto adminRoleDto = new UserRoleDto().setRoleName("ADMIN");
        UserRoleEntity adminRoleEntity = new UserRoleEntity().setRole(UserRoleEnum.ADMIN);
        when(roleRepository.findByRole(UserRoleEnum.ADMIN)).thenReturn(adminRoleEntity);
        when(roleRepository.findByRole(UserRoleEnum.MODERATOR)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.MODERATOR));
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        serviceToTest.changeRole(adminRoleDto, testUser.getUsername());

        assertTrue(testUser.getRoles().contains(adminRoleEntity));
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void changeRoleFromAdminToUser() {
        UserEntity testUser = createTestAdmin();
        UserRoleDto userRoleDto = new UserRoleDto().setRoleName("USER");
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        serviceToTest.changeRole(userRoleDto, testUser.getUsername());

        assertTrue(testUser.getRoles().isEmpty());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void changeRoleFromUserToModerator() {
        UserEntity testUser = createTestUser();
        UserRoleDto userRoleDto = new UserRoleDto().setRoleName("MODERATOR");
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(roleRepository.findByRole(UserRoleEnum.MODERATOR)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.MODERATOR));

        serviceToTest.changeRole(userRoleDto, testUser.getUsername());

        assertTrue(testUser.getRoles().contains(roleRepository.findByRole(UserRoleEnum.MODERATOR)));
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void changeRoleFromModeratorToUser() {
        UserEntity testUser = createTestModerator();
        UserRoleDto userRoleDto = new UserRoleDto().setRoleName("USER");
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        serviceToTest.changeRole(userRoleDto, testUser.getUsername());

        assertTrue(testUser.getRoles().isEmpty());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testChangeRoleWhenUserNotFound() {
        String nonExistingUsername = "nonexistentUser";
        UserRoleDto userRoleDto = new UserRoleDto().setRoleName("ADMIN");

        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> serviceToTest.changeRole(userRoleDto, nonExistingUsername));
    }

    @Test
    void testFindEmailWhenEmailExists() {
        String existingEmail = "test@example.com";
        when(userRepository.findByEmail(existingEmail)).thenReturn(Optional.of(createTestUser()));

        boolean emailFound = serviceToTest.findEmail(existingEmail);

        assertTrue(emailFound);
    }

    @Test
    void testFindEmailWhenEmailDoesNotExist() {
        String nonExistingEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        boolean emailFound = serviceToTest.findEmail(nonExistingEmail);

        assertFalse(emailFound);
    }

    @Test
    void testChangePassword() {
        String newPassword = "newPassword";
        UserEntity existingUser = createTestUser().setPassword(newPassword);
        String username = existingUser.getUsername();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        serviceToTest.changePassword(username, newPassword);

        assertEquals("encodedPassword", existingUser.getPassword());
    }

    @Test
    void testIsActive() {
        UserEntity activeUser = createTestUser();
        String username = activeUser.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(activeUser));

        boolean isActive = serviceToTest.isActive(username);

        assertTrue(isActive);
    }

    @Test
    void testIsActiveWhenUserNotFound() {
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean isActive = serviceToTest.isActive(username);

        assertTrue(isActive);
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
//        userRepository.save(userEntity);
        return userEntity;
    }
}
