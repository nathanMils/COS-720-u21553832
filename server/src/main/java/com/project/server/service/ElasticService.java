package com.project.server.service;

import com.project.server.repository.UserRepository;
import com.project.server.request.elastic.SendUserAlertRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final Logger logger = LoggerFactory.getLogger(ElasticService.class);

    @Transactional
    public void sendUserAlert(SendUserAlertRequest request) {
        userRepository.findById(request.getUserId()).ifPresentOrElse(
                (user) -> {
                    emailService.sendUserAlert(user.getEmail(), user.getUsername());
                },
                () -> {
                    logger.atError().log("User not found");
                }
        );
    }
}
