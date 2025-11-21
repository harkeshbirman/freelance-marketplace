package com.marketplace.projects.service;

import com.marketplace.projects.dto.AuthPrincipal;
import com.marketplace.projects.dto.FreelancerMatchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class FreelanceServiceCommunicator {

    private final RestTemplate restTemplate;
    private final String freelancerServiceUrl;

    public FreelanceServiceCommunicator(
            RestTemplateBuilder builder,
            @Value("${service.freelancer.baseURL}") String freelancerServiceUrl) {
        this.restTemplate = builder.build();
        this.freelancerServiceUrl = freelancerServiceUrl;
    }


    public List<FreelancerMatchResponse> findMatchingFreelancers(Long projectId) {
        try {
            String url = freelancerServiceUrl + "/" + projectId + "/matching-projects";

            AuthPrincipal authPrincipal = (AuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String token = authPrincipal.getToken();

            log.debug("Fetching freelancers from: {}", url);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + token);
            HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<List<FreelancerMatchResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to fetch responses: {}", e.getMessage());
            throw e;
        }
    }

}
