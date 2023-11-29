package com.example.project.service;

import com.example.project.model.entity.UserActivationCodeEntity;
import com.example.project.model.events.UserRegisteredEvent;
import com.example.project.repository.UserActivationCodeRepository;
import com.example.project.repository.UserRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

@Service
public class UserActivationService {
    private static final String ACTIVATION_CODE_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789";
    private static final int ACTIVATION_CODE_LENGTH = 20;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserActivationCodeRepository userActivationCodeRepository;
    public UserActivationService(EmailService emailService, UserRepository userRepository, UserActivationCodeRepository userActivationCodeRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userActivationCodeRepository = userActivationCodeRepository;
    }

    @EventListener(UserRegisteredEvent.class)
    void userRegistered(UserRegisteredEvent event){
        String activationCode = createActivationCode(event.getUserEmail());
        emailService.sendRegistrationEmail(event.getUserEmail(),
                event.getUserNames(),
                activationCode);
    }

    public void cleanUpObsoleteActivationLinks(){
        //TODO
    }
    public String createActivationCode(String userEmail) {

        String activationCode = generateActivationCode();

        UserActivationCodeEntity userActivationCodeEntity = new UserActivationCodeEntity();
        userActivationCodeEntity.setActivationCode(activationCode);
        userActivationCodeEntity.setCreated(Instant.now());
        userActivationCodeEntity.setUser(userRepository.findByEmail(userEmail).get()); //todo

        userActivationCodeRepository.save(userActivationCodeEntity);

        return activationCode;

    }

    private static String generateActivationCode() {

        StringBuilder activationCode = new StringBuilder();
        Random random = new SecureRandom();

        for (int i = 0; i < ACTIVATION_CODE_LENGTH; i++) {
            int randInx = random.nextInt(ACTIVATION_CODE_SYMBOLS.length());
            activationCode.append(ACTIVATION_CODE_SYMBOLS.charAt(randInx));
        }

        return activationCode.toString();
    }
    public void activateUser(String activationCode){
        UserActivationCodeEntity byActivationCode = userActivationCodeRepository.findByActivationCode(activationCode).get(); //TODO
        byActivationCode.getUser().setActive(true);
        userRepository.save(byActivationCode.getUser());
    }


}
