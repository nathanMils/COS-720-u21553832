package com.project.server.intergration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.server.model.entity.*;
import com.project.server.model.entity.Module;
import com.project.server.model.enums.RoleEnum;
import com.project.server.model.enums.StatusEnum;
import com.project.server.repository.*;
import com.project.server.request.admin.CreateCourseRequest;
import com.project.server.service.EmailService;
import com.project.server.service.TokenService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailServiceMock;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseModeratorRepository courseModeratorRepository;

    @Autowired
    private StudentApplicationRepository applicationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    private UUID courseId;
    private Long studentApplicationId;
    private String adminToken;
    private String studentTokenA;
    private String courseModeratorToken;
    private String moduleModeratorToken;

    @BeforeEach
    public void setUp() {

        courseId = courseRepository.saveAndFlush(
                Course.builder()
                        .name("Test Course")
                        .description("This is a test course")
                        .build()
        ).getId();
        // Generate tokens
        adminToken = tokenService.genToken(
                userRepository.saveAndFlush(
                        User.builder()
                                .username("NathanTestAdmin")
                                .password(passwordEncoder.encode("password"))
                                .email("email@email.com")
                                .firstName("Nathan")
                                .lastName("Name")
                                .enabled(true)
                                .role(RoleEnum.ROLE_ADMIN)
                                .secret("This is the secret")
                                .build()
                )
        );
        UUID moduleId = moduleRepository.save(
                Module.builder()
                        .name("Test Module")
                        .description("This is a test module")
                        .course(courseRepository.findById(courseId).orElseThrow())
                        .build()
        ).getId();
        studentTokenA = tokenService.genToken(
                userRepository.save(
                        User.builder()
                                .username("NathanTestStudent")
                                .password(passwordEncoder.encode("password"))
                                .email("email@email.com")
                                .firstName("Nathan")
                                .lastName("Name")
                                .enabled(true)
                                .role(RoleEnum.ROLE_STUDENT)
                                .secret("This is the secret")
                                .build()
                )
        );
        courseModeratorToken = tokenService.genToken(
                userRepository.save(
                        User.builder()
                                .username("NathanTestCourseModerator")
                                .password(passwordEncoder.encode("password"))
                                .email("thisemail@gmail.com")
                                .enabled(true)
                                .role(RoleEnum.ROLE_COURSE_MODERATOR)
                                .firstName("Nathan")
                                .lastName("Name")
                                .secret("This is the secret")
                                .build()
                )
        );
        courseModeratorRepository.save(
                CourseModerator.builder()
                        .user(userRepository.findByUsername("NathanTestCourseModerator").orElseThrow())
                        .course(courseRepository.findById(courseId).orElseThrow())
                        .build()
        );
        studentApplicationId = applicationRepository.save(
                StudentApplication.builder()
                        .user(userRepository.findByUsername("NathanTestStudent").orElseThrow())
                        .status(StatusEnum.PENDING)
                        .course(courseRepository.findById(courseId).orElseThrow())
                        .build()
        ).getId();
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testAcceptStudentSuccess() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/admin/acceptStudent")
                                .contentType("application/json")
                                .content("{\"applicationId\":\"" + studentApplicationId + "\"}")
                                .cookie(
                                        new Cookie("accessToken", adminToken)
                                )
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("SUCCESS"));
    }

    @Test
    public void testAcceptStudentFail() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/admin/acceptStudent")
                                .contentType("application/json")
                                .content("{\"applicationId\":\"" + 1131312412L + "\"}")
                                .cookie(
                                        new Cookie("accessToken", adminToken)
                                )
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("APPLICATION_NOT_FOUND"));
    }

    @Test
    public void testAcceptStudentInvalidAccess() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/admin/acceptStudent")
                                .contentType("application/json")
                                .content("{\"applicationId\":\"" + studentApplicationId + "\"}")
                                .cookie(
                                        new Cookie("accessToken", studentTokenA)
                                )
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
