package com.project.server.service;

import com.project.server.model.entity.User;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OTPService {
    @Value("${app.otp.length}")
    private String codeLength;

    private final CodeGenerator codeGenerator;

    private final SecretGenerator secretGenerator;

    private final CodeVerifier codeVerifier;

    public String generateSecret() {
        return secretGenerator.generate();
    }
    public String generateConfirmationCode(User user) throws CodeGenerationException {
        return codeGenerator.generate(user.getSecret(), Integer.parseInt(codeLength));
    }
    public boolean isOTPValid(String secret, String code) {
        return codeVerifier.isValidCode(secret,code);
    }
}
