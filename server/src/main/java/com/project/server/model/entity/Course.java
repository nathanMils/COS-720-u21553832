package com.project.server.model.entity;

import com.project.server.converter.StringConverter;
import com.project.server.model.dto.CourseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COURSE")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            nullable = false,
            unique = true
    )
    @Convert(converter = StringConverter.class)
    private String name;

    @Column(
            nullable = false
    )
    @Convert(converter = StringConverter.class)
    private  String description;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.DETACH,
                    CascadeType.REFRESH
            })
    @JoinTable(
            name = "course_modules",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private Set<Module> modules;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> student;

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

    public CourseDTO convert() {
        return CourseDTO.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }
}