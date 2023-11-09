package com.example.project.service;


import com.example.project.model.dtos.UserRegistrationDTO;
import com.example.project.model.entity.UserEntity;
import com.example.project.model.events.UserRegisteredEvent;
import com.example.project.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher applicationEventPublisher;


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void registerUser(UserRegistrationDTO userRegistrationDTO) {

        UserEntity userEntity = new UserEntity().
                setUsername(userRegistrationDTO.getUsername()).
                setFirstName(userRegistrationDTO.getFirstName()).
                setLastName(userRegistrationDTO.getLastName()).
                setEmail(userRegistrationDTO.getEmail()).
                setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .setActive(false);

        userRepository.save(userEntity);
        applicationEventPublisher.publishEvent(new UserRegisteredEvent(
                "UserService",
                userRegistrationDTO.getEmail(),
                userRegistrationDTO.getUsername()
        ));
    }

}
