package com.candidate.pks.Interview.repository;

import com.candidate.pks.Interview.model.Interview;
import com.candidate.pks.Interview.model.InterviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {

    @Query("SELECT i FROM Interview i WHERE i.interviewerName.id = ?1 AND i.interviewDate BETWEEN ?2 AND ?3 AND i.interviewStatus = ?4")
    Page<Interview> findByInterviewerIdAndInterviewDateBetweenAndInterviewStatus(Integer interviewerId, Date fromDate, Date toDate, InterviewStatus status, PageRequest pageRequest);

    @Query("SELECT i FROM Interview i WHERE i.interviewerName.id = ?1 AND i.interviewDate >= ?2 AND i.interviewStatus = ?3")
    Page<Interview> findUpcomingInterviewsByInterviewerIdAndInterviewStatus(Integer interviewerId, Date currentDate, InterviewStatus status, PageRequest pageRequest);
}
