package com.example.service.impl;

import com.example.entity.Student;
import com.example.repository.StudentRepository;
import com.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student generateAndSaveStudent(Student student) {
        // Đếm tổng số sinh viên hiện tại để sinh mã tiếp theo
        long count = studentRepository.count(); // ví dụ: 0 → ST00001
        String code = String.format("ST%05d", count + 1); // ST00001, ST00002
        student.setCode(code);
        return studentRepository.save(student);
    }

    @Override
    public Student findByCode(String code) {
        return studentRepository.findByCode(code);
    }

    @Override
    public Student update(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteByCode(String code) {
        Student student = studentRepository.findByCode(code);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with code: " + code);
        }
        studentRepository.delete(student);
    }

}
