package com.candidate.pks.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorPOJA {

    private String error_description;
    private String user_description;

    private String code;

    private String date;

    private String request;
}
