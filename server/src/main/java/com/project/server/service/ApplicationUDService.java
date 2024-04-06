package com.project.server.service;

import com.project.server.model.entity.User;
import com.project.server.model.enums.StatusEnum;
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
    private ModuleModeratorRepository moduleModeratorRepository;
    @Autowired
    private CourseModeratorRepository courseModeratorRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                user.isEnabled(),
                getAuthorities(user)
        );
    }

    @Transactional
    private Collection<? extends GrantedAuthority> getAuthorities(User user)
    {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        switch (user.getRole()) {
            case ROLE_STUDENT:
                authorities.addAll(
                        studentApplicationRepository.findByUserIdAndStatus(user.getId(), StatusEnum.ACCEPTED)
                                .stream()
                                .map(application -> new SimpleGrantedAuthority(String.format("course_%s_student",application.getCourse().getId().toString())))
                                .toList()
                );
                studentRepository.findByUserId(user.getId()).forEach(
                        (student) -> {
                            System.out.println(String.format("course_%s_module_%s_student",student.getCourse().getId().toString(),student.getModule().getId().toString()));
                            authorities.add(
                                    new SimpleGrantedAuthority(String.format("course_%s_module_%s_student",student.getCourse().getId().toString(),student.getModule().getId().toString()))
                            );
                        }
                );
                break;
            case ROLE_MODULE_MODERATOR:
                moduleModeratorRepository.findByUserId(user.getId()).forEach(
                        (moduleModerator) -> {
                            authorities.add(
                                    new SimpleGrantedAuthority(String.format("module_%s_moderator",moduleModerator.getModule().getId().toString()))
                            );
                        }
                );
                break;
            case ROLE_COURSE_MODERATOR:
                courseModeratorRepository.findByUserId(user.getId()).forEach(
                        (courseModerator) -> {
                            authorities.add(
                                    new SimpleGrantedAuthority(String.format("course_%s_moderator",courseModerator.getCourses().getId().toString()))
                            );
                        }
                );
                break;
            default:
                break;
        }
        return authorities;
    }
}
