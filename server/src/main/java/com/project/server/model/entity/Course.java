package com.project.server.model.entity;

import com.project.server.converter.StringConverter;
import com.project.server.model.dto.CourseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Module> modules;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
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
