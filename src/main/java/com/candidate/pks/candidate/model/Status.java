package com.candidate.pks.candidate.model;

public enum Status {
    APPLICATION_RECEIVED,    // When the application is received and acknowledged.
    INTERVIEW_SCHEDULED,     // When an interview has been scheduled.
    INTERVIEW_COMPLETED,     // When the interview has been completed.
    OFFER_EXTENDED,          // When an offer is extended to the candidate.
    OFFER_ACCEPTED,          // When the candidate accepts the offer.
    OFFER_REJECTED,          // When the candidate rejects the offer.
    REJECTED,                // When the candidate is rejected at any stage.
    QUALIFIED_FOR_NEXT_ROUND // When the candidate qualifies for the next round.
}


