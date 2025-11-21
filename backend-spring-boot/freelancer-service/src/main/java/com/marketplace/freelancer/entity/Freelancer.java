package com.marketplace.freelancer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "freelancers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Freelancer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @Column(nullable = false)
    private String name;

    @Column(name = "experience_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FreelancerSkill> skills = new HashSet<>();

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private Set<Bid> bids = new HashSet<>();


}