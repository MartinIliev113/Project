package com.example.project.model.events;

import org.springframework.context.ApplicationEvent;

public class ForgotPasswordEvent extends ApplicationEvent {
    private final String userEmail;
    private final String userName;
    public ForgotPasswordEvent(Object source, String userEmail, String userName) {
        super(source);
        this.userEmail = userEmail;
        this.userName = userName;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }
}
