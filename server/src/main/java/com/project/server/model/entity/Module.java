package com.project.server.model.entity;

import com.project.server.converter.StringConverter;
import com.project.server.model.dto.ModuleDTO;
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
@Table(name = "MODULE")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "modules")
    private Set<Course> courses;

    @Convert(converter = StringConverter.class)
    private String description;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts;

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

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> student;

    public ModuleDTO convert() {
        return ModuleDTO.builder()
                .moduleId(id)
                .moduleName(name)
                .build();
    }
}