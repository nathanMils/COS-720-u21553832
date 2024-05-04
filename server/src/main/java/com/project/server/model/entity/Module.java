package com.project.server.model.entity;

import com.project.server.converter.StringConverter;
import com.project.server.model.dto.ModuleDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Convert(converter = StringConverter.class)
    private String description;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<Student> students;

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

    public ModuleDTO convert() {
        return ModuleDTO.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }
}
