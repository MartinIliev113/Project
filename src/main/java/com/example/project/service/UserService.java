package com.example.project.service;


import com.example.project.model.dtos.UserDTO;
import com.example.project.model.dtos.UserRoleDto;
import com.example.project.model.entity.UserEntity;
import com.example.project.model.entity.UserRoleEntity;
import com.example.project.model.enums.UserRoleEnum;
import com.example.project.model.events.ForgotPasswordEvent;
import com.example.project.model.events.UserRegisteredEvent;
import com.example.project.model.exceptions.ObjectNotFoundException;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.project.model.exceptions.ExceptionMessages.USER_NOT_FOUND;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, ApplicationEventPublisher applicationEventPublisher, ModelMapper modelMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
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
        return moderatosList.stream().map(moderator->modelMapper.map(moderator,UserDTO.class)).toList();
    }
    public List<UserDTO> getAdmins() {
        List<UserEntity> adminList = userRepository.findByRolesContaining(roleRepository.findByRole(UserRoleEnum.valueOf("ADMIN")));
        return adminList.stream().map(admins->modelMapper.map(admins,UserDTO.class)).toList();
    }
    public List<UserDTO> getUsers() {
        List<UserEntity> usersList = userRepository.findByRolesNotContaining(roleRepository.findByRole(UserRoleEnum.valueOf("MODERATOR")));
        return usersList.stream().map(user->modelMapper.map(user,UserDTO.class)).toList();
    }

    public UserDTO findByUsername(String username) {
        return modelMapper.map(userRepository.findByUsername(username)
                        .orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND)),
                UserDTO.class);
    }

    public void changeRole(UserRoleDto userRoleDto, String username) {

        UserEntity user;
        if(userRepository.findByUsername(username).isEmpty()){
            throw new ObjectNotFoundException(USER_NOT_FOUND);
        }else {
             user = userRepository.findByUsername(username).orElseThrow(()->new ObjectNotFoundException(USER_NOT_FOUND));
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

    public void changePassword(String username, String password) {
        userRepository.findByUsername(username)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(password));
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));
    }

    public boolean isActive(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        return user.map(UserEntity::isActive).orElse(true);
    }
}
