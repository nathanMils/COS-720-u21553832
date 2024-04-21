package com.project.server.intergration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.server.model.entity.*;
import com.project.server.model.entity.Module;
import com.project.server.model.enums.RoleEnum;
import com.project.server.model.enums.StatusEnum;
import com.project.server.repository.*;
import com.project.server.request.courseModerator.CreateModuleRequest;
import com.project.server.service.EmailService;
import com.project.server.service.TokenService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourseModeratorIntegrationTests {

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
    private UUID moduleId;
    private Long studentApplicationId;
    private String studentTokenA;
    private String studentTokenB;
    private String courseModeratorTokenA;
    private String courseModeratorTokenB;
    private String moduleModeratorTokenA;
    private String moduleModeratorTokenB;

    @BeforeEach
    public void setUp() {

        courseId = courseRepository.saveAndFlush(
                Course.builder()
                        .name("Test Course")
                        .description("This is a test course")
                        .build()
        ).getId();

        studentTokenA = tokenService.genToken(
                userRepository.save(
                        User.builder()
                                .firstName("Student")
                                .lastName("Test")
                                .username("studentTest1")
                                .role(RoleEnum.ROLE_STUDENT)
                                .email("emailtest@gmail.com")
                                .enabled(true)
                                .secret("secret")
                                .password(passwordEncoder.encode("studentTest!1"))
                                .build()
                )
        );

        studentTokenB = tokenService.genToken(
                userRepository.save(
                        User.builder()
                                .firstName("Student")
                                .lastName("Test")
                                .username("studentTest2")
                                .role(RoleEnum.ROLE_STUDENT)
                                .email("emailtest@gmail.com")
                                .enabled(true)
                                .secret("secret")
                                .password(passwordEncoder.encode("studentTest!2"))
                                .build()
                )
        );

        courseModeratorTokenA = tokenService.genToken(
                userRepository.save(
                        User.builder()
                                .firstName("Course")
                                .lastName("Moderator")
                                .username("courseModerator")
                                .role(RoleEnum.ROLE_COURSE_MODERATOR)
                                .email("emailtest@gmail.com")
                                .enabled(true)
                                .secret("secret")
                                .password(passwordEncoder.encode("passwordCourseModerator!1"))
                                .build()
                )
        );

        courseModeratorTokenB = tokenService.genToken(
                userRepository.save(
                        User.builder()
                                .firstName("Course")
                                .lastName("Moderator")
                                .username("courseModerator2")
                                .role(RoleEnum.ROLE_COURSE_MODERATOR)
                                .email("emailtest@gmail.com")
                                .enabled(true)
                                .secret("secret")
                                .password(passwordEncoder.encode("passwordCourseModerator!2"))
                                .build()
                )
        );

        moduleId = moduleRepository.save(
                Module.builder()
                        .name("Test Module")
                        .description("This is a test module")
                        .course(courseRepository.findById(courseId).orElseThrow())
                        .build()
        ).getId();

        moduleModeratorTokenA = tokenService.genToken(
                userRepository.save(
                        User.builder()
                                .firstName("Module")
                                .lastName("Moderator")
                                .username("moduleModerator")
                                .role(RoleEnum.ROLE_MODULE_MODERATOR)
                                .email("emailtest@gmail.com")
                                .enabled(true)
                                .secret("secret")
                                .password(passwordEncoder.encode("passwordModuleModerator!1"))
                                .build()
                )
        );

        moduleModeratorTokenB = tokenService.genToken(
                userRepository.save(
                        User.builder()
                                .firstName("Module")
                                .lastName("Moderator")
                                .username("moduleModerator2")
                                .role(RoleEnum.ROLE_MODULE_MODERATOR)
                                .email("emailtest@gmail.com")
                                .enabled(true)
                                .secret("secret")
                                .password(passwordEncoder.encode("passwordModuleModerator!2"))
                                .build()
                )
        );

        moduleModeratorRepository.save(
                ModuleModerator.builder()
                        .module(moduleRepository.findById(moduleId).orElseThrow())
                        .user(userRepository.findByUsername("moduleModerator").orElseThrow())
                        .build()
        );

        courseModeratorRepository.save(
                CourseModerator.builder()
                        .course(courseRepository.findById(courseId).orElseThrow())
                        .user(userRepository.findByUsername("courseModerator").orElseThrow())
                        .build()
        );

        applicationRepository.save(
                StudentApplication.builder()
                        .course(courseRepository.findById(courseId).orElseThrow())
                        .user(userRepository.findByUsername("studentTest1").orElseThrow())
                        .status(StatusEnum.ACCEPTED)
                        .build()
        );
    }

    @Test
    public void testFetchModulesInCourseSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/courseModerator/fetchModules/" + courseId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", courseModeratorTokenA)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].moduleId").value(moduleId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].moduleName").value("Test Module"));
    }

    @Test
    public void testFetchModulesInCourseFailure() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/courseModerator/fetchModules/" + UUID.randomUUID().toString())
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", courseModeratorTokenA)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"));
    }

    @Test
    public void testFetchModulesCourseInvalidAccessCourseModeratorFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/courseModerator/fetchModules/" + courseId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", courseModeratorTokenB)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("ACCESS_DENIED"));
    }

    @Test
    public void testFetchModulesCourseInvalidAccessStudentFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/courseModerator/fetchModules/" + courseId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", studentTokenA)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testFetchModulesCourseInvalidAccessModuleModeratorFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/courseModerator/fetchModules/" + courseId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", moduleModeratorTokenA)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testCreateModuleSuccess() throws Exception {
        CreateModuleRequest request = new CreateModuleRequest("Test Module 2", "This is a test module 2");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/courseModerator/createModule/" + courseId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", courseModeratorTokenA)
                        )
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.moduleId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.moduleName").value("Test Module 2"));
    }

    @Test
    public void testCreateModuleDuplicateFailure() throws Exception {
        CreateModuleRequest request = new CreateModuleRequest("Test Module", "This is a test module");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/courseModerator/createModule/" + courseId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", courseModeratorTokenA)
                        )
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("MODULE_NAME_EXISTS"));
    }

    @Test
    public void testCreateModuleInvalidAccessCourseModeratorFail() throws Exception {
        CreateModuleRequest request = new CreateModuleRequest("Test Module 2", "This is a test module 2");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/courseModerator/createModule/" + courseId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", courseModeratorTokenB)
                        )
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testCreateModuleInvalidAccessStudentFail() throws Exception {
        CreateModuleRequest request = new CreateModuleRequest("Test Module 2", "This is a test module 2");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/courseModerator/createModule/" + courseId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", studentTokenA)
                        )
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testCreateModuleInvalidAccessModuleModeratorFail() throws Exception {
        CreateModuleRequest request = new CreateModuleRequest("Test Module 2", "This is a test module 2");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/courseModerator/createModule/" + courseId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", moduleModeratorTokenA)
                        )
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testDeleteModuleSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/courseModerator/deleteModule/" + courseId + "/" + moduleId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", courseModeratorTokenA)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("SUCCESS"));
    }

    @Test
    public void testDeleteModuleFailure() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/courseModerator/deleteModule/" + courseId + "/" + UUID.randomUUID().toString())
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", courseModeratorTokenA)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("MODULE_NOT_FOUND"));
    }

    @Test
    public void testDeleteModuleInvalidAccessCourseModeratorFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/courseModerator/deleteModule/" + courseId + "/" + moduleId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", courseModeratorTokenB)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("ACCESS_DENIED"));
    }

    @Test
    public void testDeleteModuleInvalidAccessStudentFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/courseModerator/deleteModule/" + courseId + "/" + moduleId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", studentTokenA)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("ACCESS_DENIED"));
    }

    @Test
    public void testDeleteModuleInvalidAccessModuleModeratorFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/courseModerator/deleteModule/" + courseId + "/" + moduleId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", moduleModeratorTokenA)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("ACCESS_DENIED"));
    }

    @Test
    public void testDeleteModuleInvalidCourseFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/courseModerator/deleteModule/" + UUID.randomUUID().toString() + "/" + moduleId)
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", courseModeratorTokenA)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("ACCESS_DENIED"));
    }

    @Test
    public void testDeleteModuleInvalidModuleFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/courseModerator/deleteModule/" + courseId + "/" + UUID.randomUUID().toString())
                        .contentType("application/json")
                        .cookie(
                                new Cookie("accessToken", courseModeratorTokenA)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.internalCode").value("MODULE_NOT_FOUND"));
    }
}
