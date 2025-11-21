package com.marketplace.freelancer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FreelancerDto {
    private Long id;
    private Long userId;
    private String experienceLevel;
    private List<String> skills;
}
