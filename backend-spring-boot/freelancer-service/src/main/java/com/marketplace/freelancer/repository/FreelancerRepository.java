package com.marketplace.freelancer.repository;

import com.marketplace.freelancer.entity.Freelancer;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    boolean existsByUserId(Long userId);

    Optional<Freelancer> findByUserId(@NotNull(message = "Freelancer ID is required") Long freelancerId);
}