package com.project.server.model.entity;


import com.project.server.model.dto.LectureDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "LECTURE",
        uniqueConstraints = @UniqueConstraint(columnNames = {"module_id", "fileName"})
)
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            nullable = false
    )
    private String fileName;

    @Column(
            nullable = false
    )
    private String fileType;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @Lob
    @Column(
            nullable = false
    )
    private byte[] content;

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

    public LectureDTO convert() {
        return LectureDTO.builder()
                .id(this.id)
                .fileName(this.fileName)
                .createdAt(this.createdAt)
                .build();
    }
}
