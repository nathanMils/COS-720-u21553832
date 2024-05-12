package com.project.server.service.emailservice;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.server.service.emailservice.impl.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
@Profile("prod")
@RequiredArgsConstructor
@Slf4j
public class SESEmailService implements EmailService {
    private final AmazonSimpleEmailService sesClient = AmazonSimpleEmailServiceClientBuilder.defaultClient();
    @Value("${spring.mail.username}")
    private String emailSource;
    @Value("${app.base.url}")
    private String baseUrl;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void sendDeviceVerificationEmail(String userEmail, String code) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void sendModeratorEmail(String userEmail, String username, String password, String firstName, String lastName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void sendEmailVerificationEmail(String userEmail, String code) {
        try {
            String templateName = "EmailVerificationTemplate";

            // Check if the template exists, if not create it
            if (doesTemplateExist(templateName)) {
                Template template = new Template()
                        .withTemplateName(templateName)
                        .withSubjectPart("Confirm Your Email")
                        .withHtmlPart(loadEmailHtml("verifyEmailTemplate"));

                CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest().withTemplate(template);
                sesClient.createTemplate(createTemplateRequest);
            }

            // Prepare the dynamic content for the template
            Map<String, String> templateData = new HashMap<>();
            templateData.put("link", baseUrl + "/verifyEmail?token=" + code);

            String templateDataJson = objectMapper.writeValueAsString(templateData);
            // Send the email
            SendTemplatedEmailRequest request = new SendTemplatedEmailRequest()
                    .withDestination(new Destination().withToAddresses(userEmail))
                    .withSource(emailSource)
                    .withTemplate(templateName)
                    .withTemplateData(templateDataJson);

            sesClient.sendTemplatedEmail(request);
        } catch (IOException e) {
            log.error("Failed to load email template", e);
        }
    }

    @Override
    public void sendUserAlert(String userEmail, String userName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void sendPasswordResetEmail(String userEmail, String token) {
        try {
            String templateName = "PasswordResetTemplate";

            // Check if the template exists, if not create it
            if (doesTemplateExist(templateName)) {
                Template template = new Template()
                        .withTemplateName(templateName)
                        .withSubjectPart("Reset Your Password")
                        .withHtmlPart(loadEmailHtml("resetTemplate"));

                CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest().withTemplate(template);
                sesClient.createTemplate(createTemplateRequest);
            }

            // Prepare the dynamic content for the template
            Map<String, String> templateData = new HashMap<>();
            templateData.put("url", baseUrl + "/reset?token=" + token);

            String templateDataJson = objectMapper.writeValueAsString(templateData);
            // Send the email
            SendTemplatedEmailRequest request = new SendTemplatedEmailRequest()
                    .withDestination(new Destination().withToAddresses(userEmail))
                    .withSource(emailSource)
                    .withTemplate(templateName)
                    .withTemplateData(templateDataJson);

            sesClient.sendTemplatedEmail(request);
        } catch (IOException e) {
            log.error("Failed to load email template", e);
        }
    }

    private boolean doesTemplateExist(String templateName) {
        try {
            sesClient.getTemplate(new GetTemplateRequest().withTemplateName(templateName));
            return false;
        } catch (TemplateDoesNotExistException e) {
            return true;
        }
    }

    private String loadEmailHtml(String template) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/" + template + ".html");
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
