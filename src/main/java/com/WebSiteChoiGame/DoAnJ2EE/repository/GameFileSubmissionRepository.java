package com.WebSiteChoiGame.DoAnJ2EE.repository;

import com.WebSiteChoiGame.DoAnJ2EE.entity.GameFileSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
 
public interface GameFileSubmissionRepository extends JpaRepository<GameFileSubmission, Integer> {
    List<GameFileSubmission> findByDeveloper(User developer);
} 