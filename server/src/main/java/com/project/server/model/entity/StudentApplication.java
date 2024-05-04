package com.project.server.model.entity;

import com.project.server.model.dto.StudentApplicationDTO;
import com.project.server.model.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "STUDENT_APPLICATION",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"})
)
public class StudentApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

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

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false
    )
    private StatusEnum status;

    public StudentApplicationDTO convert() {
        return StudentApplicationDTO.builder()
                .applicationId(id)
                .courseId(course.getId())
                .username(user.getUsername())
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .courseName(course.getName())
                .description(course.getDescription())
                .status(status)
                .build();
    }
}
