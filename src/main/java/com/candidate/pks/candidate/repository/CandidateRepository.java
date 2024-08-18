package com.candidate.pks.candidate.repository;

import com.candidate.pks.candidate.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate,Integer> {
    @Query("select c from Candidate c where c.candidateId = ?1")
    Optional<Candidate> findByCandidateId(String candidateId);
}
