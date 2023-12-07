package com.example.marketplace.service;


import com.example.marketplace.model.entity.UserActivationCodeEntity;
import com.example.marketplace.model.entity.UserForgotPasswordCodeEntity;
import com.example.marketplace.model.events.ForgotPasswordEvent;
import com.example.marketplace.model.events.UserRegisteredEvent;
import com.example.marketplace.model.exceptions.ObjectNotFoundException;
import com.example.marketplace.repository.UserActivationCodeRepository;
import com.example.marketplace.repository.UserForgotPasswordCodeRepository;
import com.example.marketplace.repository.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

import static com.example.marketplace.model.exceptions.ExceptionMessages.USER_NOT_FOUND;


@Service
public class UserActivationService {
    private static final String CODE_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 20;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserActivationCodeRepository userActivationCodeRepository;
    private final UserForgotPasswordCodeRepository userForgotPasswordCodeRepository;

    public UserActivationService(EmailService emailService, UserRepository userRepository, UserActivationCodeRepository userActivationCodeRepository, UserForgotPasswordCodeRepository userForgotPasswordCodeRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userActivationCodeRepository = userActivationCodeRepository;
        this.userForgotPasswordCodeRepository = userForgotPasswordCodeRepository;
    }

    @EventListener(UserRegisteredEvent.class)
    void userRegistered(UserRegisteredEvent event) {
        String activationCode = createActivationCode(event.getUserEmail());
        emailService.sendRegistrationEmail(event.getUserEmail(),
                event.getUserNames(),
                activationCode);
    }

    @EventListener(ForgotPasswordEvent.class)
    void forgotPassword(ForgotPasswordEvent event) {
        String forgotPasswordCode = createForgotPasswordCode(event.getUserEmail());
        emailService.sendForgotPassWordEmail(event.getUserEmail(), event.getUserName(), forgotPasswordCode);
    }

    public void cleanUpObsoleteActivationLinks() {
        userActivationCodeRepository.deleteAll();
    }

    public String createActivationCode(String userEmail) {

        String activationCode = generateCode();

        UserActivationCodeEntity userActivationCodeEntity = new UserActivationCodeEntity();
        userActivationCodeEntity.setActivationCode(activationCode);
        userActivationCodeEntity.setCreated(Instant.now());
        userActivationCodeEntity.setUser(userRepository.findByEmail(userEmail).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND)));

        userActivationCodeRepository.save(userActivationCodeEntity);

        return activationCode;

    }

    public String createForgotPasswordCode(String userEmail) {

        String forgotPasswordCode = generateCode();

        UserForgotPasswordCodeEntity userForgotPasswordCodeEntity = new UserForgotPasswordCodeEntity();
        userForgotPasswordCodeEntity.setCode(forgotPasswordCode);
        userForgotPasswordCodeEntity.setCreated(Instant.now());
        userForgotPasswordCodeEntity.setUser(userRepository.findByEmail(userEmail).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND)));

        userForgotPasswordCodeRepository.save(userForgotPasswordCodeEntity);

        return forgotPasswordCode;

    }

    private static String generateCode() {

        StringBuilder activationCode = new StringBuilder();
        Random random = new SecureRandom();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randInx = random.nextInt(CODE_SYMBOLS.length());
            activationCode.append(CODE_SYMBOLS.charAt(randInx));
        }

        return activationCode.toString();
    }

    public void activateUser(String activationCode) {
        UserActivationCodeEntity byActivationCode = userActivationCodeRepository.findByActivationCode(activationCode).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND));
        byActivationCode.getUser().setActive(true);
        userRepository.save(byActivationCode.getUser());
    }


    public String getUserNameByForgotPasswordCode(String forgotPasswordCode) {
        return userForgotPasswordCodeRepository.findByCode(forgotPasswordCode).orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND)).getUser().getUsername();
    }
}
