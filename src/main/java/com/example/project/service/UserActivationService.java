package com.example.project.service;

import com.example.project.model.events.UserRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserActivationService {
    private final EmailService emailService;

    public UserActivationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener(UserRegisteredEvent.class)
    void userRegistered(UserRegisteredEvent event){
        //todo: add activiaton links
        System.out.println("user with email "+event.getUserEmail());
        emailService.sendRegistrationEmail(event.getUserEmail(), event.getUserNames());
    }

    public void cleanUpObsoleteActivationLinks(){
        //TODO
    }
}
