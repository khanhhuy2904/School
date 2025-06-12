package com.school.management.controller;

import com.school.management.model.Teacher;

import java.util.List;

public interface TeacherService {
    List<Teacher> getAll();

    Teacher getByCode(String code);

    void create(Teacher teacher);

    void edit(Teacher teacher);

    void deleteByCode(String code);
}
