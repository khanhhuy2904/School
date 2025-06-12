package com.school.management.exceptions;

public class StudentDuplicateException extends RuntimeException {

    public StudentDuplicateException(String message) {
        super(message);
    }
}
