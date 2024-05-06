package com.project.server.service;

import com.project.server.model.enums.StatusEnum;
import com.project.server.model.projections.authorization.AuthModuleProjection;
import com.project.server.model.projections.authorization.AuthUserProjection;
import com.project.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userDetailsService")
@Transactional
public class ApplicationUDService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentApplicationRepository studentApplicationRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseModeratorRepository courseModeratorRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AuthUserProjection user = userRepository.findAuthProjectedByUsername(username).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                user.getEnabled(),
                getAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(AuthUserProjection user)
    {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        switch (user.getRole()) {
            case ROLE_STUDENT:
                authorities.addAll(
                        studentApplicationRepository.findAuthProjectionByUserIdAndStatus(user.getId(), StatusEnum.ACCEPTED)
                                .stream()
                                .map(application -> new SimpleGrantedAuthority(String.format("course_%s_student",application.getCourse().getId().toString())))
                                .toList()
                );
                studentRepository.findAuthProjectionByUserId(user.getId()).forEach(
                        student -> authorities.addAll(
                                List.of(
                                    new SimpleGrantedAuthority(String.format("course_%s_module_%s_student",student.getCourse().getId().toString(),student.getModule().getId().toString())),
                                    new SimpleGrantedAuthority(String.format("module_%s_student",student.getModule().getId().toString()))
                                )
                        )
                );
                break;
            case ROLE_COURSE_MODERATOR:
                courseModeratorRepository.findProjectedByUserId(user.getId()).forEach(
                        courseModerator -> {
                            authorities.add(
                                new SimpleGrantedAuthority(String.format("course_%s_moderator",courseModerator.getCourse().getId().toString()))
                            );
                            for (AuthModuleProjection module: courseModerator.getCourse().getModules()) {
                                authorities.add(
                                    new SimpleGrantedAuthority(String.format("module_%s_moderator",module.getId().toString()))
                                );
                            }
                        }
                );
                break;
            default:
                break;
        }
        return authorities;
    }
}
