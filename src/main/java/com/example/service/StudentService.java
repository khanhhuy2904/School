package com.example.service;

import com.example.entity.Student;

import java.util.List;

public interface StudentService {

    List<Student> findAll();

    Student saveStudent(Student student);

    Student generateAndSaveStudent(Student student);

    Student findByCode(String code);
    Student update(Student student);
    void deleteByCode(String code);
}
