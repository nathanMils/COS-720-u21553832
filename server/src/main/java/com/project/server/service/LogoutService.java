package com.project.server.service;

import com.project.server.repository.RefreshTokenRepository;
import com.project.server.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        Cookie[] cookies = request.getCookies();
        response.setStatus(204);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    String username = tokenService.getUserName(token);
                    log.info("User {} logged out successfully", username);

                    // Create new cookie to remove the old one
                    Cookie newCookie = new Cookie(cookie.getName(), null);
                    newCookie.setMaxAge(0);
                    newCookie.setPath("/");
                    response.addCookie(newCookie);
                } else if ("refreshToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    refreshTokenRepository.findByToken(token).ifPresentOrElse(
                            (RToken) -> {
                                RToken.setRevoked(true);
                                refreshTokenRepository.save(
                                        RToken
                                );
                                log.info("Refresh token for user {} revoked", RToken.getUser().getUsername());
                            },
                            () -> {
                                throw new EntityNotFoundException("TOKEN_NOT_FOUND");
                            }
                    );

                    // Create new cookie to remove the old one
                    Cookie newCookie = new Cookie(cookie.getName(), null);
                    newCookie.setMaxAge(0);
                    newCookie.setPath("/");
                    response.addCookie(newCookie);
                }
            }
        }
    }
}