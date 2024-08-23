package com.candidate.pks.Interview.repository;

import com.candidate.pks.Interview.dto.FetchScheduleRequest;
import com.candidate.pks.Interview.model.Interview;
import com.candidate.pks.Interview.model.InterviewStatus;
import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.candidate.model.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {

    @Query("SELECT i FROM Interview i WHERE i.interviewerName.id = ?1 AND i.interviewDate BETWEEN ?2 AND ?3 AND i.interviewStatus = ?4")
    Page<Interview> findByInterviewerIdAndInterviewDateBetweenAndInterviewStatus(Integer interviewerId, Date fromDate, Date toDate, InterviewStatus status, PageRequest pageRequest);

    @Query("SELECT i FROM Interview i WHERE i.interviewerName.id = ?1 AND i.interviewDate >= ?2 AND i.interviewStatus = ?3")
    Page<Interview> findUpcomingInterviewsByInterviewerIdAndInterviewStatus(Integer interviewerId, Date currentDate, InterviewStatus status, PageRequest pageRequest);

    @Override
    Optional<Interview> findById(Integer integer);

    Optional<Interview> findByCandidateAndInterviewerName(Candidate candidate, Employee interviewer);
    Optional<Interview> findByCandidate(Candidate candidate);


//    @Query("SELECT i FROM Interview i WHERE i.interviewDate >= ?1 AND i.interviewStatus = ?2")
//    Page<Interview> findByInterviewDateAndStatus(Date fromDate, InterviewStatus interviewStatus, Pageable pageable);
//
//    @Query("SELECT i FROM Interview i WHERE i.interviewDate >= ?1")
//    Page<Interview> findByInterviewDate(Date fromDate, Pageable pageable);
//
//    @Query("SELECT i FROM Interview i WHERE i.interviewStatus = ?1")
//    Page<Interview> findByInterviewStatus(InterviewStatus interviewStatus, Pageable pageable);
//
//    @Override
//    Page<Interview> findAll(Pageable pageable);
//
//
    @Query("SELECT i FROM Interview i WHERE i.interviewDate >= ?1 AND i.interviewStatus = ?2")
    Page<Interview> findByInterviewDateAndStatus(Date fromDate, InterviewStatus interviewStatus, Pageable pageable);

    @Query("SELECT i FROM Interview i WHERE i.interviewDate >= ?1")
    Page<Interview> findByInterviewDate(Date fromDate, Pageable pageable);

    @Query("SELECT i FROM Interview i WHERE i.interviewStatus = ?1")
    Page<Interview> findByInterviewStatus(InterviewStatus interviewStatus, Pageable pageable);

    @Query("SELECT i FROM Interview i WHERE i.interviewerName.id = ?1 AND i.interviewDate >= ?2 AND i.interviewStatus = ?3")
    Page<Interview> findByInterviewerIdAndInterviewDateAndStatus(Integer interviewerId, Date fromDate, InterviewStatus interviewStatus, Pageable pageable);

    @Query("SELECT i FROM Interview i WHERE i.interviewerName.id = ?1 AND i.interviewDate >= ?2")
    Page<Interview> findByInterviewerIdAndInterviewDate(Integer interviewerId, Date fromDate, Pageable pageable);

    @Query("SELECT i FROM Interview i WHERE i.interviewerName.id = ?1 AND i.interviewStatus = ?2")
    Page<Interview> findByInterviewerIdAndInterviewStatus(Integer interviewerId, InterviewStatus interviewStatus, Pageable pageable);

    @Query("SELECT i FROM Interview i WHERE i.interviewerName.id = ?1")
    Page<Interview> findByInterviewerId(Integer interviewerId, Pageable pageable);

    @Override
    Page<Interview> findAll(Pageable pageable);

}
