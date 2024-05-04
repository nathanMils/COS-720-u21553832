package com.project.server.service;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OTPService {
    @Value("${app.otp.length}")
    private String codeLength;

    private final CodeGenerator codeGenerator;

    private final QrGenerator qrGenerator;

    private final SecretGenerator secretGenerator;

    private final CodeVerifier codeVerifier;

    public String generateSecret() {
        return secretGenerator.generate();
    }

    public String generateQRCode(String secret) throws QrGenerationException {
        QrData qrData = new QrData.Builder()
                .label("2FA Server")
                .issuer("Portal")
                .secret(secret)
                .digits(Integer.parseInt(codeLength))
                .period(30)
                .algorithm(HashingAlgorithm.SHA1)
                .build();
        return Utils.getDataUriForImage(qrGenerator.generate(qrData), qrGenerator.getImageMimeType());
    }
    public boolean isOTPValid(String secret, String code) {
        return codeVerifier.isValidCode(secret,code);
    }
}
