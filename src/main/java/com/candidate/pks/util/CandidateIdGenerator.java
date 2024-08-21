package com.candidate.pks.util;

import com.candidate.pks.candidate.model.SequenceGenerator;
import com.candidate.pks.candidate.repository.SequenceGeneratorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CandidateIdGenerator {

    @Autowired
    private SequenceGeneratorRepository sequenceGeneratorRepository;

    @Transactional
    public String generateCandidateId() {
        SequenceGenerator sequenceGenerator = sequenceGeneratorRepository.findByIdOrderBySequenceValueDesc()
                .orElseGet(() -> {
                    SequenceGenerator newSequence = new SequenceGenerator();
                    newSequence.setSequenceValue(0L);
                    return sequenceGeneratorRepository.save(newSequence);
                });

        Long nextValue = sequenceGenerator.getSequenceValue() + 1;
        sequenceGenerator.setSequenceValue(nextValue);
        sequenceGeneratorRepository.save(sequenceGenerator);

        return String.format("CD%04d", nextValue);
    }
}
