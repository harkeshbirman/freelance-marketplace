package com.marketplace.freelancer.service;

import com.marketplace.freelancer.dto.*;
import com.marketplace.freelancer.entity.*;
import com.marketplace.freelancer.exception.DuplicateResourceException;
import com.marketplace.freelancer.exception.ResourceNotFoundException;
import com.marketplace.freelancer.repository.BidRepository;
import com.marketplace.freelancer.repository.FreelancerRepository;
import com.marketplace.freelancer.repository.ProjectSkillFreelancerRepository;
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
@Transactional
public class FreelancerService {

    private final FreelancerRepository freelancerRepository;
    private final BidRepository bidRepository;
    private final ProjectSkillFreelancerRepository projectSkillFreelancerRepository;
    private final ProjectServiceCommunicator projectServiceCommunicator;

    public FreelancerDto registerFreelancer(FreelancerRegistrationRequest request) {
        log.info("Registering freelancer for user ID: {}", request.getUserId());

        if (freelancerRepository.existsByUserId(request.getUserId())) {
            throw new DuplicateResourceException("Freelancer already registered for user ID: " + request.getUserId());
        }

        Freelancer freelancer = new Freelancer();
        freelancer.setUserId(request.getUserId());
        freelancer.setName(request.getName());
        freelancer.setExperienceLevel(request.getExperienceLevel());
        freelancer.setSkills(
                request.getSkills()
                        .stream()
                        .map(skill -> new FreelancerSkill(null, freelancer, skill))
                        .collect(Collectors.toSet()));

        Freelancer saved = freelancerRepository.save(freelancer);
        log.info("Freelancer registered successfully with ID: {}", saved.getId());

        return new FreelancerDto(
                saved.getId(),
                saved.getUserId(),
                saved.getExperienceLevel().name(),
                saved.getSkills().stream()
                        .map(FreelancerSkill::getSkillName)
                        .toList()
        );
    }

    public BidResponse submitBid(BidRequest request) {
        log.info("Submitting bid for project {} by freelancer {}", request.getProjectId(), request.getFreelancerId());

        Freelancer freelancer = freelancerRepository.findByUserId(request.getFreelancerId())
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found"));

        if (bidRepository.existsByProjectIdAndFreelancerId(request.getProjectId(), request.getFreelancerId())) {
            throw new DuplicateResourceException("Bid already submitted for this project");
        }

        ProjectResponse projectResponse = projectServiceCommunicator.fetchProjectByID(request.getProjectId());

        ProjectSkillFreelancer projectSkillFreelancer = calculateMatchingScore(
                projectResponse.getSkills(),
                freelancer.getSkills()
        );

        Bid bid = new Bid();
        bid.setProjectId(request.getProjectId());
        bid.setFreelancer(freelancer);
        bid.setBidAmount(request.getBidAmount());
        bid.setMessage(request.getMessage());
        bid.setStatus(BidStatus.PENDING);

        Bid savedBid = bidRepository.save(bid);

        if (projectSkillFreelancer.getMatchScore() == 0.0) {
            log.info("Bid rejected due to zero matching score for project {} by freelancer {}",
                    request.getProjectId(), request.getFreelancerId());
            savedBid.setStatus(BidStatus.REJECTED);
            bidRepository.save(savedBid);

            return new BidResponse(
                    savedBid.getId(),
                    "Bid rejected due to no matching skills.",
                    savedBid.getStatus(),
                    savedBid.getBidAmount(),
                    savedBid.getProjectId(),
                    savedBid.getFreelancer().getId()
            );
        } else {
            projectSkillFreelancer.setProjectId(request.getProjectId());
            projectSkillFreelancer.setFreelancerId(request.getFreelancerId());
            projectSkillFreelancer.setComment(request.getMessage());

            projectSkillFreelancerRepository.save(projectSkillFreelancer);
        }
        return new BidResponse(
                savedBid.getId(),
                "Bid submitted successfully.",
                savedBid.getStatus(),
                savedBid.getBidAmount(),
                savedBid.getProjectId(),
                savedBid.getFreelancer().getId()
        );
    }

    private ProjectSkillFreelancer calculateMatchingScore(List<String> requiredSkills, Set<FreelancerSkill> freelancerSkills) {
        Set<FreelancerSkill> matchingSkillsSet = freelancerSkills.stream()
                .filter(fs -> requiredSkills.contains(fs.getSkillName()))
                .collect(Collectors.toSet());

        long matchingSkillsCount = matchingSkillsSet.size();
        Double matchScore = (double) matchingSkillsCount / (double) requiredSkills.size();

        ProjectSkillFreelancer projectSkillFreelancer = new ProjectSkillFreelancer();
        projectSkillFreelancer.setMatchingSkills(matchingSkillsSet);
        projectSkillFreelancer.setMatchScore(matchScore);

        return projectSkillFreelancer;
    }

    public List<MatchingProjectsResponse> getMatchingProjects(Long projectId) {
        List<ProjectSkillFreelancer> matchingRecords = projectSkillFreelancerRepository.findByProjectId(projectId);

        if (matchingRecords.isEmpty()) {
            log.info("No matching freelancers found for project ID: {}", projectId);
            throw new ResourceNotFoundException("No matching freelancers found for project ID: " + projectId);
        }
        

        return matchingRecords.stream()
                .map(record -> new MatchingProjectsResponse(
                        record.getProjectId(),
                        record.getFreelancerId(),
                        freelancerRepository.findByUserId(record.getFreelancerId()).orElseThrow().getName(),
                        record.getMatchScore(),
                        record.getMatchingSkills().stream()
                                .map(FreelancerSkill::getSkillName)
                                .toList()
                ))
                .collect(Collectors.toList());
    }

}
