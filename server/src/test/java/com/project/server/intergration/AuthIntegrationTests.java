package com.project.server.intergration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.server.model.entity.User;
import com.project.server.model.enums.RoleEnum;
import com.project.server.repository.UserRepository;
import com.project.server.request.auth.ApplicationRequest;
import com.project.server.request.auth.LoginRequest;
import com.project.server.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


/**
 * This class contains integration tests for the authentication functionality.
 * It uses Spring Boot's testing support to mock the MVC layer and interact with the application as if it were running in a servlet container.
 * The tests are ordered and the context is dirtied after each test method to ensure isolation between tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private EmailService emailServiceMock;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This method sets up the test environment before each test.
     * It saves a user to the repository and mocks the EmailService.
     */
    @BeforeEach
    public void setUp() {
        userRepository.saveAndFlush(
                User.builder()
                        .username("NathanTest")
                        .password(passwordEncoder.encode("password"))
                        .email("email@email.com")
                        .firstName("Nathan")
                        .lastName("Name")
                        .enabled(true)
                        .role(RoleEnum.ROLE_STUDENT)
                        .secret("This is the secret")
                        .build()
        );
        emailServiceMock = Mockito.mock(EmailService.class);
        userRepository.save(
                User.builder()
                        .username("NathanTestNotVerified")
                        .password(passwordEncoder.encode("password"))
                        .email("email@email.com")
                        .firstName("Nathan")
                        .lastName("Name")
                        .enabled(false)
                        .role(RoleEnum.ROLE_STUDENT)
                        .secret("This is the secret")
                        .build()
        );
    }

    /**
     * This test verifies that a user can successfully log in with correct credentials.
     */
    @Test
    void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("NathanTest", "password");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLoginRequest = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/login")
                                .contentType("application/json")
                                .content(jsonLoginRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("SUCCESS"));
    }

    /**
     * This test verifies that a login attempt with a correct username but incorrect password fails.
     */
    @Test
    void testLoginFailCorrectUsername() throws Exception {
        LoginRequest loginRequest = new LoginRequest("NathanTest123", "password123");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLoginRequest = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/login")
                                .contentType("application/json")
                                .content(jsonLoginRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("INVALID_CREDENTIALS"));
    }

    /**
     * This test verifies that a login attempt with an incorrect username but correct password fails.
     */
    @Test
    void testLoginFailCorrectPassword() throws Exception {
        LoginRequest loginRequest = new LoginRequest("NathanTest123", "password");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLoginRequest = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/login")
                                .contentType("application/json")
                                .content(jsonLoginRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("INVALID_CREDENTIALS"));
    }

    /**
     * This test verifies that a login attempt with both incorrect username and password fails.
     */
    @Test
    void testLoginFailInCorrectUsernameAndPassword() throws Exception {
        LoginRequest loginRequest = new LoginRequest("NathanTest123", "password123");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLoginRequest = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/login")
                                .contentType("application/json")
                                .content(jsonLoginRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("INVALID_CREDENTIALS"));
    }

    /**
     * This test verifies that a login attempt fails if the user is not enabled.
     */
    @Test
    void testLoginFailUserNotEnabled() throws Exception {
        LoginRequest loginRequest = new LoginRequest("NathanTestNotVerified", "password");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLoginRequest = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/login")
                                .contentType("application/json")
                                .content(jsonLoginRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("EMAIL_NOT_VERIFIED"));
    }

    /**
     * This test verifies that a login attempt fails if the user is not verified and the password is incorrect.
     */
    @Test
    void testLoginFailUserNotVerifiedIncorrectPassword() throws Exception {
        LoginRequest loginRequest = new LoginRequest("NathanTestNotVerified", "password111");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLoginRequest = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/login")
                                .contentType("application/json")
                                .content(jsonLoginRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("INVALID_CREDENTIALS"));
    }

    /**
     * This test verifies that a user can successfully apply with valid credentials.
     */
    @Test
    void testApplySuccess() throws Exception {
        ApplicationRequest applicationRequest = new ApplicationRequest("NotIsNathanTest", "passWord1!", "nathangenius@gmail.com", "Nathan", "Opppermans");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/apply")
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(applicationRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("SUCCESS"));
    }

    /**
     * This test verifies that an application fails if the username already exists.
     */
    @Test
    void testApplyFailUsernameExists() throws Exception {
        ApplicationRequest applicationRequest = new ApplicationRequest("NathanTest", "passWord1!", "nathangenius@gmail.com", "Nathan", "Opppermans");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/apply")
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(applicationRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("USERNAME_EXISTS"));
    }

    /**
     * This test verifies that an application fails if the validation fails.
     */
    @Test
    void testApplyFailValidation() throws Exception {
        ApplicationRequest applicationRequest = new ApplicationRequest("NathanTesting", "password", "nathangenius@gmail.com", "Nathan", "Opppermans");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/apply")
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(applicationRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("VALIDATION_FAILED"));
    }
}
