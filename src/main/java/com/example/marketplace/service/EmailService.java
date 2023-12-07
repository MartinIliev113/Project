package com.example.marketplace.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final String projectEmail;

    public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender,
                        @Value("{mail.project}") String projectEmail) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.projectEmail = projectEmail;
    }

    public void sendRegistrationEmail(String userEmail, String username, String activationCode) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setFrom(projectEmail);
            mimeMessageHelper.setReplyTo(projectEmail);
            mimeMessageHelper.setSubject("Welcome to project!");
            mimeMessageHelper.setText(generateRegistrationEmailBody(username, activationCode), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendForgotPassWordEmail(String userEmail, String username, String forgotPasswordCode) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setFrom(projectEmail);
            mimeMessageHelper.setReplyTo(projectEmail);
            mimeMessageHelper.setSubject("Forgotten password!");
            mimeMessageHelper.setText(generateForgotPasswordEmailBody(username, forgotPasswordCode), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateForgotPasswordEmailBody(String username, String forgotPasswordCode) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("forgot_password_code", forgotPasswordCode);

        return templateEngine.process("email/forgot-email", context);

    }

    private String generateRegistrationEmailBody(String username, String activationCode) {

        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("activation_code", activationCode);

        return templateEngine.process("email/registration-email", context);
    }

}
