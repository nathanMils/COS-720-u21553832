package com.project.server.intergration;

import com.project.server.model.entity.User;
import com.project.server.model.enums.RoleEnum;
import com.project.server.repository.UserRepository;
import com.project.server.request.auth.LoginRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository.saveAndFlush(
                User.builder()
                        .username("NathanOpp")
                        .password("password")
                        .email("email@email.com")
                        .firstName("Nathan")
                        .lastName("Name")
                        .enabled(true)
                        .role(RoleEnum.ROLE_STUDENT)
                        .secret("This is the secret")
                        .build()
        );
    }

    @Test
    public void testYourEndpoint() throws Exception {
        mockMvc.perform(
                    MockMvcRequestBuilders.post(
                        "/api/v1/auth/login",
                            LoginRequest.builder()
                                    .username("NathanOpp")
                                    .password("password")
                    )
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Expected Response"));
    }
}
