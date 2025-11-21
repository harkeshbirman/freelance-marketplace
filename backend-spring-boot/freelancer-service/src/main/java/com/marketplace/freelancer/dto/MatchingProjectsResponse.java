package com.marketplace.freelancer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchingProjectsResponse {
    private Long projectId;
    private Long freelancerId;
    private String freelancerName;
    private Double matchScore;
    private List<String> matchingSkills;
}
