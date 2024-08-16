package com.candidate.pks.exception;

public class CandidateNotFoundException  extends RuntimeException{
    public CandidateNotFoundException(String message) {
        super(message);
    }
}
