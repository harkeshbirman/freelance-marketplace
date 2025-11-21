package com.marketplace.freelancer.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidRequest {

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Freelancer ID is required")
    private Long freelancerId;

    @NotNull(message = "Bid amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Bid amount must be positive")
    private BigDecimal bidAmount;

    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message cannot exceed 2000 characters")
    private String message;

}
