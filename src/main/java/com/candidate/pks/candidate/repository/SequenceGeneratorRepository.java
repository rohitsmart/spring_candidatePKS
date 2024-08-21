package com.candidate.pks.candidate.repository;

import com.candidate.pks.candidate.model.SequenceGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SequenceGeneratorRepository extends JpaRepository<SequenceGenerator,Integer> {


    @Query("select s from SequenceGenerator s  order by s.sequenceValue DESC")
    Optional<SequenceGenerator> findByIdOrderBySequenceValueDesc();
}
