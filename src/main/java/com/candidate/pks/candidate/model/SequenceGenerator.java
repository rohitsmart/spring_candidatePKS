package com.candidate.pks.candidate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sequence_generator")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SequenceGenerator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private Long sequenceValue;
}
