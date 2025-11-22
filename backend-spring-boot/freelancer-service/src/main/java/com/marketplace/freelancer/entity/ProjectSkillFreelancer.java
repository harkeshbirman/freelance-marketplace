package com.marketplace.freelancer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;


@Entity
@Table(
        name = "project_skill_freelancer",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "freelancer_id"})
)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSkillFreelancer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "freelancer_id", nullable = false)
    private Long freelancerId;

    @Column(name = "match_score", nullable = false)
    private Double matchScore;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "bid_amount", nullable = false)
    private BigDecimal bidAmount;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<FreelancerSkill> matchingSkills;

}