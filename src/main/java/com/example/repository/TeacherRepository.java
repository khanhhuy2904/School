package com.example.repository;

import com.example.entity.Student;
import com.example.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByCode(String code);

}

