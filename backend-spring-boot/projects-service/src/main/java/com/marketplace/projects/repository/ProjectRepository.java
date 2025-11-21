package com.marketplace.projects.repository;

import com.marketplace.projects.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByClientId(Long clientId);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.skills WHERE p.id = :projectId")
    Optional<Project> findByIdWithSkills(@Param("projectId") Long projectId);

}
