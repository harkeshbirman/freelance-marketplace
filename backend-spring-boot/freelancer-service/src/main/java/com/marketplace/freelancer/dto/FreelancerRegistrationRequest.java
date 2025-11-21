package com.marketplace.freelancer.dto;

import com.marketplace.freelancer.entity.ExperienceLevel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerRegistrationRequest {

    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Name can not be null")
    @Size(min = 1, max = 30)
    private String name;

    @NotNull(message = "Experience level is required")
    private ExperienceLevel experienceLevel;

    @NotEmpty(message = "Skills list cannot be empty")
    private List<String> skills;
}
