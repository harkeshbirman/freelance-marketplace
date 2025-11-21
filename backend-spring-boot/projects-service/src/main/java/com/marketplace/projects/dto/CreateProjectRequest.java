package com.marketplace.projects.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProjectRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    private String description;

    @NotNull(message = "Client user ID is required")
    @Positive(message = "Client user ID must be positive")
    private Long userId;

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Budget must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Budget must have at most 8 integer digits and 2 decimal places")
    private BigDecimal budget;

    @NotEmpty(message = "At least one skill is required")
    @Size(min = 1, max = 20, message = "Project must have between 1 and 20 skills")
    private List<String> skills;
}