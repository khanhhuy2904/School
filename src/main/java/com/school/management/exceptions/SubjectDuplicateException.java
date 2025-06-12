package com.school.management.exceptions;

public class SubjectDuplicateException extends RuntimeException {
    public SubjectDuplicateException(String message) {
        super(message);
    }
}
