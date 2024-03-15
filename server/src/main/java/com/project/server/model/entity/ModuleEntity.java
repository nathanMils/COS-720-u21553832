package com.project.server.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "module")
public class ModuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long moduleId;

    @Column(name = "NAME", unique = true, nullable = false)
    private String moduleName;

    @ManyToMany(mappedBy = "modules")
    private Set<UserEntity> students;

    @OneToMany(mappedBy = "module")
    private List<AnnouncementEntity> announcements;
}
