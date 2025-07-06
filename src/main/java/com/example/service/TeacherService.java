package com.example.service;
import com.example.entity.Teacher;
import java.util.List;

public interface TeacherService {
    List<Teacher> findAll();
    Teacher generateAndSaveTeacher(Teacher teacher);
    Teacher findByCode (String code);
    Teacher update (Teacher teacher);
    void deleteByCode(String code);

}
