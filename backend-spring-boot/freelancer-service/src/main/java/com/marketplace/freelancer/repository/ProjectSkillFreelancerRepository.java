package com.marketplace.freelancer.repository;

import com.marketplace.freelancer.entity.ProjectSkillFreelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectSkillFreelancerRepository extends JpaRepository<ProjectSkillFreelancer, Long> {
    List<ProjectSkillFreelancer> findByProjectId(Long projectId);
}