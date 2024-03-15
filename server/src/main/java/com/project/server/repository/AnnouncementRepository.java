package com.project.server.repository;

import com.project.server.model.entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity,Long> {
}
