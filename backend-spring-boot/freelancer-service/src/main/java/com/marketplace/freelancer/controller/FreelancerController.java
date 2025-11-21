package com.marketplace.freelancer.controller;

import com.marketplace.freelancer.dto.*;
import com.marketplace.freelancer.service.FreelancerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/freelancers")
@RequiredArgsConstructor
public class FreelancerController {

    private final FreelancerService freelancerService;

    @PostMapping("/register")
    public ResponseEntity<FreelancerDto> registerFreelancer(@Valid @RequestBody FreelancerRegistrationRequest request) {
        FreelancerDto freelancer = freelancerService.registerFreelancer(request);
        return new ResponseEntity<>(freelancer, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}/matching-projects")
    public ResponseEntity<List<MatchingProjectsResponse>> getMatchingProjects(@PathVariable Long projectId) {
        List<MatchingProjectsResponse> projects = freelancerService.getMatchingProjects(projectId);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/bids/new")
    public ResponseEntity<BidResponse> submitBid(@Valid @RequestBody BidRequest request) {
        BidResponse response = freelancerService.submitBid(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}