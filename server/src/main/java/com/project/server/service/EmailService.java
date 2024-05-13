package com.project.server.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailSource;
    @Value("${app.base.url}")
    private String baseUrl;
    private final ResourceLoader resourceLoader;

    @Async
    public void sendDeviceVerificationEmail(String userEmail, String code) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(emailSource);
            helper.setTo(userEmail);
            helper.setSubject("Security Alert: New Sign-in detected");
            String content = loadEmailHtml("verificationDeviceTemplate");
            message.setContent(
                    content.replace("[[VERIFICATION_CODE]]",code),
                    "text/html; charset=utf-8"
            );
            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            emailFailed();
        }
    }

    @Async
    public void sendModeratorEmail(
            String userEmail,
            String username,
            String password,
            String firstName,
            String lastName
    ) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(emailSource);
            helper.setTo(userEmail);
            helper.setSubject("Moderator Account Created");
            String content = loadEmailHtml("moderatorTemplate");
            message.setContent(
                    content
                            .replace("[[PASSWORD]]",password)
                            .replace("[[USERNAME]]",username)
                            .replace("[[FIRST_NAME]]",firstName)
                            .replace("[[LAST_NAME]]",lastName),
                    "text/html; charset=utf-8"
            );
            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            emailFailed();
        }
    }

    @Async
    public void sendEmailVerificationEmail(String userEmail, String code) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(emailSource);
            helper.setTo(userEmail);
            helper.setSubject("Confirm Your Email");
            String content = loadEmailHtml("verifyEmailTemplate");
            message.setContent(
                    content.replace("{{URL}}",baseUrl+"/verifyEmail?token="+code),
                    "text/html; charset=utf-8"
            );
            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            emailFailed();
        }
    }

    @Async
    public void sendUserAlert(String userEmail, String userName) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(emailSource);
            helper.setTo(userEmail);
            helper.setSubject("Security Alert: New Sign-in detected");
            String content = loadEmailHtml("userNotification");
            message.setContent(
                    content.replace("[User]",userName).replace("[Email Address]",userEmail),
                    "text/html; charset=utf-8"
            );
            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            emailFailed();
        }
    }

    @Async
    public void sendPasswordResetEmail(String userEmail, String token) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(emailSource);
            helper.setTo(userEmail);
            helper.setSubject("Reset Your Password");
            String content = loadEmailHtml("resetTemplate");
            message.setContent(
                    content.replace("{{URL}}",baseUrl+"/reset?token="+token),
                    "text/html; charset=utf-8"
            );
            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            emailFailed();
        }
    }

    public String loadEmailHtml(String template) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/"+template+".html");
        InputStream inputStream = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private void emailFailed() {
        log.atInfo().log("Email Failed");
    }
}
