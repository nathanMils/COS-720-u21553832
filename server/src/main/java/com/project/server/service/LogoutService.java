package com.project.server.service;

import com.project.server.model.entity.RefreshToken;
import com.project.server.repository.RefreshTokenRepository;
import com.project.server.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    public final UserRepository userRepository;
    public final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer")) {
            final String token = header.substring(7);
            final String username = tokenService.getUserName(token);
            refreshTokenRepository.findByUser(
                    userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND")))
                    .ifPresent(
                            (rToken) ->{
                                rToken.setRevoked(true);
                                refreshTokenRepository.save(rToken);
                            }
            );
        }
    }
}
