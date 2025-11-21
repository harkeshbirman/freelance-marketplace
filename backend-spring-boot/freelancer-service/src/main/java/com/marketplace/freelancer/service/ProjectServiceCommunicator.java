package com.marketplace.freelancer.service;

import com.marketplace.freelancer.dto.AuthPrincipal;
import com.marketplace.freelancer.dto.ProjectResponse;
import com.marketplace.freelancer.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ProjectServiceCommunicator {
    private final RestTemplate restTemplate;
    private final String projectServiceURL;

    public ProjectServiceCommunicator(
            RestTemplateBuilder builder,
            @Value("${service.projects.baseURL}") String projectServiceURL) {

        this.restTemplate = builder.build();
        this.projectServiceURL = projectServiceURL;
    }

    ProjectResponse fetchProjectByID(Long projectId) {
        try {
            String url = projectServiceURL + "/get/" + projectId;
            AuthPrincipal authPrincipal = (AuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String token = authPrincipal.getToken();

            log.debug("Fetching freelancers from: {}", url);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + token);
            HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<ProjectResponse> projectResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    ProjectResponse.class
            );

            if (projectResponse.getStatusCode().is2xxSuccessful()) {
                return projectResponse.getBody();
            } else {
                throw new ResourceNotFoundException("Failed to fetch project with ID: " + projectId);
            }
        } catch (ResourceNotFoundException e) {
            log.error("Error fetching project by ID {}: {}", projectId, e.getMessage());
            throw e;
        }
    }
}
