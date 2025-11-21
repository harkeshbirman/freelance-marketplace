package com.marketplace.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreelancerMatchResponse {
    private Long projectId;
    private Long freelancerId;
    private Double matchScore;
    private List<String> matchingSkills;
}