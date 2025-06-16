package com.school.management.controller;

import com.school.management.model.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> getAll();

    Subject getByCode(String code);

    void create(Subject subject);

    void edit(Subject subject);

    void deleteByCode(String code);
    void assignTeacherToSubject(String subjectCode, String teacherCode);
    void assignStudentsToSubject(String subjectCode, List<String> studentCodes);

}
