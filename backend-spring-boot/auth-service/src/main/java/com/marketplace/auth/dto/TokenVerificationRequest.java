package com.marketplace.auth.dto;

import lombok.Data;

@Data
public class TokenVerificationRequest {
    private String token;
}
