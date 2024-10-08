package com.project.server.model.entity;

import com.project.server.converter.StringConverter;
import com.project.server.model.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            unique = true,
            nullable = false
    )
    private String username;

    @Column(
            nullable = false
    )
    @Convert(converter = StringConverter.class)
    private String email;

    @Column(nullable = false)
    @Convert(converter = StringConverter.class)
    private String password;

    @Column(nullable = false)
    @Convert(converter = StringConverter.class)
    private String firstName;

    @Column(nullable = false)
    @Convert(converter = StringConverter.class)
    private String lastName;

    @Column(
            nullable = false
    )
    private String secret;


    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false
    )
    private RoleEnum role;

    @CreationTimestamp
    @Column(
            updatable = false,
            nullable = false,
            name = "created_at"
    )
    private Date createdAt;

    @UpdateTimestamp
    @Column(
            nullable = false,
            name = "updated_at"
    )
    private Date updatedAt;

    @Column(nullable = false)
    private boolean enabled;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> student;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentApplication> studentApplication;
}
