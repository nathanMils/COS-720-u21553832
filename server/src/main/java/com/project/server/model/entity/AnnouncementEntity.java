package com.project.server.model.entity;

import com.project.server.converter.StringConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "announcement")
public class AnnouncementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long annId;

    @Column(nullable = false)
    @Convert(converter = StringConverter.class)
    private String announcement;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private ModuleEntity module;
}
