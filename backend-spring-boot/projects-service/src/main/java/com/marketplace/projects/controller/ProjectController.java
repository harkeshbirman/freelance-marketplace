package com.marketplace.projects.controller;

import com.marketplace.projects.dto.*;
import com.marketplace.projects.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/new")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        log.info("Received request to create project: {}", request.getTitle());
        ProjectResponse response = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/matches")
    public ResponseEntity<List<FreelancerMatchResponse>> getMatchingFreelancers(@PathVariable Long id) {
        log.info("Received request to get matches for project: {}", id);
        List<FreelancerMatchResponse> matches = projectService.getMatchingFreelancers(id);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        log.info("Received request to get project: {}", id);
        ProjectResponse project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }
 
    @GetMapping("/all")
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        log.info("Received request to get all projects");
        List<ProjectResponse> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }
}