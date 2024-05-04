package com.project.server.service;

import com.project.server.model.entity.User;
import com.project.server.model.enums.RoleEnum;
import com.project.server.model.enums.StatusEnum;
import com.project.server.repository.CourseModeratorRepository;
import com.project.server.repository.UserRepository;
import com.project.server.response.user.GetProfileResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CourseModeratorRepository courseModeratorRepository;
    @Transactional
    public GetProfileResponse getProfile() {
        User user = getAuthenticatedUser();
        int numberOfCoursesTaken = user.getStudentApplication().stream().filter(application -> application.getStatus() != StatusEnum.ACCEPTED).toList().size();
        int numberOfCoursesCreated = courseModeratorRepository.findByUserId(user.getId()).size();
        int numberOfModulesTaken = user.getStudent().size();
        int numberOfModulesCreated = courseModeratorRepository.findByUserId(user.getId()).stream().mapToInt(courseModerator -> courseModerator.getCourse().getModules().size()).sum();
        return GetProfileResponse.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .numberOfCoursesTaken(numberOfCoursesTaken)
                .numberOfCoursesCreated(numberOfCoursesCreated)
                .numberOfModulesTaken(numberOfModulesTaken)
                .numberOfModulesCreated(numberOfModulesCreated)
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Transactional
    public RoleEnum getRole() {
        User user = getAuthenticatedUser();
        return user.getRole();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userRepository.findByUsername(currentUserName).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        }
        throw new RuntimeException("ERROR");
    }
}
