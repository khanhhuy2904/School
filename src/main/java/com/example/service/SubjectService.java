package com.example.service;

import com.example.entity.Subject;
import com.example.entity.Teacher;

import java.util.List;

public interface SubjectService {
    List<Subject> findAll();
    Subject generateAndSaveSubject(Subject subject);
    void deleteByCode(String code);
    Subject findByCode(String code);
    Subject update(Subject subject);
    Subject save(Subject subject);
}

