package com.example.marketplace.service;


import com.example.marketplace.model.dtos.UserDTO;
import com.example.marketplace.model.dtos.UserRoleDto;
import com.example.marketplace.model.entity.UserActivationCodeEntity;
import com.example.marketplace.model.entity.UserEntity;
import com.example.marketplace.model.entity.UserRoleEntity;
import com.example.marketplace.model.enums.UserRoleEnum;
import com.example.marketplace.model.events.ForgotPasswordEvent;
import com.example.marketplace.model.events.UserRegisteredEvent;
import com.example.marketplace.model.exceptions.ObjectNotFoundException;
import com.example.marketplace.repository.RoleRepository;
import com.example.marketplace.repository.UserActivationCodeRepository;
import com.example.marketplace.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.marketplace.model.exceptions.ExceptionMessages.USER_NOT_FOUND;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final UserActivationCodeRepository userActivationCodeRepository;


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, ApplicationEventPublisher applicationEventPublisher, ModelMapper modelMapper, RoleRepository roleRepository, UserActivationCodeRepository userActivationCodeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.userActivationCodeRepository = userActivationCodeRepository;
    }


    public void registerUser(UserDTO userDTO) {

        UserEntity userEntity = new UserEntity().
                setUsername(userDTO.getUsername()).
                setFirstName(userDTO.getFirstName()).
                setLastName(userDTO.getLastName()).
                setEmail(userDTO.getEmail()).
                setPassword(passwordEncoder.encode(userDTO.getPassword()))
                .setActive(false);

        userRepository.save(userEntity);
        applicationEventPublisher.publishEvent(new UserRegisteredEvent(
                "UserService",
                userDTO.getEmail(),
                userDTO.getUsername()
        ));
    }

    public void passwordChangePublish(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));

        applicationEventPublisher.publishEvent(new ForgotPasswordEvent("UserService", email, user.getUsername()));
    }


    public List<UserDTO> getModerators() {
        List<UserEntity> moderatosList = userRepository.findByRolesContainingAndRolesNotContaining(roleRepository.findByRole(UserRoleEnum.valueOf("MODERATOR")),
                roleRepository.findByRole(UserRoleEnum.valueOf("ADMIN")));
        return moderatosList.stream().map(moderator -> modelMapper.map(moderator, UserDTO.class)).toList();
    }

    public List<UserDTO> getAdmins() {
        List<UserEntity> adminList = userRepository.findByRolesContaining(roleRepository.findByRole(UserRoleEnum.valueOf("ADMIN")));
        return adminList.stream().map(admins -> modelMapper.map(admins, UserDTO.class)).toList();
    }

    public List<UserDTO> getUsers() {
        List<UserEntity> usersList = userRepository.findByRolesNotContaining(roleRepository.findByRole(UserRoleEnum.valueOf("MODERATOR")));
        return usersList.stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
    }

    public UserDTO findByUsername(String username) {

//        return modelMapper.map(userRepository.findByUsername(username)
//                        .orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND)),
//                UserDTO.class);

        UserDTO userDTO;
        if (userRepository.findByUsername(username).isPresent()) {
            userDTO = modelMapper.map(userRepository.findByUsername(username), UserDTO.class);
        } else {
            throw new ObjectNotFoundException(USER_NOT_FOUND);
        }
        return userDTO;
    }

    public void changePassword(String username, String password) {
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    user.setPassword(passwordEncoder.encode(password));
                    userRepository.save(user);
                });
        userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));
    }

    public void changeRole(UserRoleDto userRoleDto, String username) {

        UserEntity user;
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new ObjectNotFoundException(USER_NOT_FOUND);
        } else {
            user = userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));
            //  user.getRoles().clear();
            user.setRoles(new ArrayList<>());
        }

        if (!userRoleDto.getRoleName().equals("ADMIN") && !userRoleDto.getRoleName().equals("MODERATOR")) {
            user.getRoles().clear();
        } else {
            UserRoleEntity role = roleRepository.findByRole(UserRoleEnum.valueOf(userRoleDto.getRoleName()));
            if (role.getRole().equals(UserRoleEnum.ADMIN)) {
                user.getRoles().clear();
                user.getRoles().add(roleRepository.findByRole(UserRoleEnum.ADMIN));
                user.getRoles().add(roleRepository.findByRole(UserRoleEnum.MODERATOR));
            } else {
                user.getRoles().clear();
                user.getRoles().add(roleRepository.findByRole(UserRoleEnum.MODERATOR));
            }
        }
        userRepository.save(user);
    }

    public Boolean findEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


    public boolean isActive(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        return user.map(UserEntity::isActive).orElse(true);
    }

    public void deleteUser(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));
        UserActivationCodeEntity userActivationCodeEntity = userActivationCodeRepository.findByUser(user).orElse(null);
        if (userActivationCodeEntity != null) {
            userActivationCodeRepository.delete(userActivationCodeEntity);
        }
        userRepository.delete(user);
    }
}
