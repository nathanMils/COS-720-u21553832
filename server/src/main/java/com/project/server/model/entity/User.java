package com.project.server.model.entity;

import com.project.server.converter.StringConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.*;

@Data
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
    @Convert(converter = StringConverter.class)
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

    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetching by default
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.DETACH,
                    CascadeType.REFRESH
            })
    @JoinTable(
            name = "students_modules",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private Set<Module> modules;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.DETACH,
                    CascadeType.REFRESH
            })
    @JoinTable(
            name = "moderator_modules",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private Set<Module> moderates;

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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RefreshToken token;
}
