package com.marketplace.freelancer.repository;

import com.marketplace.freelancer.entity.Bid;
import com.marketplace.freelancer.entity.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    boolean existsByProjectIdAndFreelancerId(Long projectId, Long freelancerId);
}