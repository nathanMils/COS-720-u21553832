package com.project.server.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailSource;
    private final ResourceLoader resourceLoader;
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

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

            logger.atInfo().log("Email Failed");
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
                    content.replace("[[URL]]","http://localhost:8080/verify?token="+code),
                    "text/html; charset=utf-8"
            );
            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            logger.atInfo().log("Email Failed");
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
            logger.atInfo().log("Email Failed");
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
}
