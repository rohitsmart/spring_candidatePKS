package com.candidate.pks.exception;

public class EmployeeNotFoundException  extends RuntimeException{
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
