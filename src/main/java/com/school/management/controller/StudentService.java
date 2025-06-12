package com.school.management.controller;

import com.school.management.model.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAll();

    Student getByCode(String code);

    void create(Student student);

    void edit(Student student);

    void deleteByCode(String code);
}
