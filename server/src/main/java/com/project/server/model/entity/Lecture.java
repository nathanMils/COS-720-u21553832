package com.project.server.model.entity;


import jakarta.persistence.*;
import lombok.*;

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
}
