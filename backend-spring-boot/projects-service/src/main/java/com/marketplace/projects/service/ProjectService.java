package com.marketplace.projects.service;

import com.marketplace.projects.dto.*;
import com.marketplace.projects.entity.*;
import com.marketplace.projects.exception.ResourceNotFoundException;
import com.marketplace.projects.repository.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository;
    private final FreelanceServiceCommunicator matchingService;
    private final MeterRegistry meterRegistry;

    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {

        try {
            log.info("Creating project with title: {} for user: {}", request.getTitle(), request.getUserId());

            // Find or create client
            Client client = clientRepository.findByUserId(request.getUserId())
                    .orElseGet(() -> {
                        log.info("Creating new client for user: {}", request.getUserId());
                        Client newClient = Client.builder()
                                .userId(request.getUserId())
                                .build();
                        return clientRepository.save(newClient);
                    });

            Set<Skill> skills = request.getSkills().stream()
                    .map(skillName -> new Skill(null, skillName))
                    .collect(Collectors.toSet());

            Project project = Project.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .client(client)
                    .budget(request.getBudget())
                    .skills(skills)
//                    .status(ProjectStatus.OPEN)
                    .build();

            Project savedProject = projectRepository.save(project);

            log.info("Project created successfully with id: {} and {} required skills",
                    savedProject.getId(), skills.size());

            // Increment metrics
            Counter.builder("projects.created")
                    .description("Total number of projects created")
                    .tag("status", "success")
                    .register(meterRegistry)
                    .increment();
 
            return mapToProjectResponse(savedProject);

        } catch (Exception e) {
            log.error("Error creating project: {}", e.getMessage(), e);
            Counter.builder("projects.created")
                    .description("Total number of projects created")
                    .tag("status", "failure")
                    .register(meterRegistry)
                    .increment();
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<FreelancerMatchResponse> getMatchingFreelancers(Long projectId) {

        try {
            log.info("Fetching matching freelancers for project: {}", projectId);

            List<FreelancerMatchResponse> matches = matchingService.findMatchingFreelancers(projectId);
            log.info("Found {} matching freelancers for project: {}", matches.size(), projectId);

            return matches;
        } catch (Exception e) {
            log.error("Error fetching matching freelancers for project {}: {}", projectId, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long projectId) {
        log.info("Fetching project with id: {}", projectId);

        Project project = projectRepository.findByIdWithSkills(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        return mapToProjectResponse(project);
    }

    private ProjectResponse mapToProjectResponse(Project project) {
        List<String> skillResponses = project.getSkills().stream()
                .map(Skill::getSkillName)
                .collect(Collectors.toList());

        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .clientId(project.getClient().getUserId())
                .budget(project.getBudget())
                .skills(skillResponses)
                .build();
    }

    @Transactional
    public List<ProjectResponse> getAllProjects() {
        log.info("Fetching all projects");

        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(this::mapToProjectResponse)
                .collect(Collectors.toList());
    }
}
