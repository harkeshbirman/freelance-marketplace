package com.marketplace.freelancer.dto;

import com.marketplace.freelancer.entity.BidStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidResponse {
    private Long bidId;
    private String message;
    private BidStatus status;
    private BigDecimal bidAmount;
    private Long projectId;
    private Long freelancerId;
}
