package com.candidate.pks.candidate.repository;

import com.candidate.pks.candidate.model.Candidate;
import com.candidate.pks.candidate.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate,Integer> {
    @Query("select c from Candidate c where c.candidateId = ?1")
    Optional<Candidate> findByCandidateId(String candidateId);

    Page<Candidate> findByApplicationDateAfter(LocalDate fromDate, Pageable pageable);

    Page<Candidate> findByStatus(Status status, Pageable pageable);

    Page<Candidate> findByApplicationDateAfterAndStatus(LocalDate fromDate, Status status, Pageable pageable);

    Page<Candidate> findByApplicationDateBetweenAndStatus(LocalDateTime fromDate, LocalDateTime toDate, Status status, Pageable pageable);

    Page<Candidate> findByApplicationDateBetween(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);

    @Query("SELECT c FROM Candidate c JOIN c.interviews i WHERE i.interviewerName.empId = :empId")
    Page<Candidate> findByInterviewScheduledForEmpId(@Param("empId") String empId, Pageable pageable);


}
