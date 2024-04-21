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
    private ModuleModeratorRepository moduleModeratorRepository;

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
        moduleModeratorToken = tokenService.genToken(
                userRepository.save(
                        User.builder()
                                .username("NathanTestModuleModerator")
                                .password(passwordEncoder.encode("password"))
                                .email("thisemail@gmail.com")
                                .firstName("Nathan")
                                .lastName("Name")
                                .role(RoleEnum.ROLE_MODULE_MODERATOR)
                                .secret("This is the secret")
                                .enabled(true)
                                .build()
                )
        );
        moduleModeratorRepository.save(
                ModuleModerator.builder()
                        .user(userRepository.findByUsername("NathanTestModuleModerator").orElseThrow())
                        .module(moduleRepository.findById(moduleId).orElseThrow())
                        .build()
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
    public void testCreateCourseSuccess() throws Exception {
        CreateCourseRequest createCourseRequest = new CreateCourseRequest("Test Course 2", "This is a test course 2");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonCreateCourseRequest = objectMapper.writeValueAsString(createCourseRequest);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/admin/createCourse")
                                .contentType("application/json")
                                .content(jsonCreateCourseRequest)
                                .cookie(
                                        new Cookie("accessToken", adminToken)
                                )
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Test Course 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value("This is a test course 2"));
    }

    @Test
    public void testCreateCourseFail() throws Exception {
        CreateCourseRequest createCourseRequest = new CreateCourseRequest("Test Course", "This is a test course");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonCreateCourseRequest = objectMapper.writeValueAsString(createCourseRequest);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/admin/createCourse")
                                .contentType("application/json")
                                .content(jsonCreateCourseRequest)
                                .cookie(
                                        new Cookie("accessToken", adminToken)
                                )
                )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("COURSE_NAME_EXISTS"));
    }

    @Test
    public void testCreateCourseInvalidAccess() throws Exception {
        CreateCourseRequest createCourseRequest = new CreateCourseRequest("Test Course 2", "This is a test course 2");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonCreateCourseRequest = objectMapper.writeValueAsString(createCourseRequest);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/admin/createCourse")
                                .contentType("application/json")
                                .content(jsonCreateCourseRequest)
                                .cookie(
                                        new Cookie("accessToken", studentTokenA)
                                )
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testDeleteCourseSuccess() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/admin/deleteCourse/" + courseId)
                                .cookie(
                                        new Cookie("accessToken", adminToken)
                                )
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("SUCCESS"));
    }

    @Test
    public void testDeleteCourseFail() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/admin/deleteCourse/" + courseId)
                                .cookie(
                                        new Cookie("accessToken", adminToken)
                                )
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("SUCCESS"));
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/admin/deleteCourse/" + courseId)
                                .cookie(
                                        new Cookie("accessToken", adminToken)
                                )
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("COURSE_NOT_FOUND"));
    }

    @Test
    public void testDeleteCourseInvalidAccess() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/admin/deleteCourse/" + courseId)
                                .cookie(
                                        new Cookie("accessToken", studentTokenA)
                                )
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
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
