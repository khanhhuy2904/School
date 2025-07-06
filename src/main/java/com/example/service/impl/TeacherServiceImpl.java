package com.example.service.impl;

import com.example.entity.Student;
import com.example.entity.Teacher;
import com.example.repository.TeacherRepository;
import com.example.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher generateAndSaveTeacher(Teacher teacher) {
        long count = teacherRepository.count();
        String code = String.format("TC%05d", count + 1);
        teacher.setCode(code);
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher findByCode(String code) {
        return teacherRepository.findByCode(code);
    }
    @Override
    public Teacher update(Teacher updatedTeacher) {
        Teacher existing = teacherRepository.findByCode(updatedTeacher.getCode());
        if (existing == null) {
            throw new RuntimeException("Teacher with code " + updatedTeacher.getCode() + " not found");
        }
        existing.setName(updatedTeacher.getName());
        existing.setGender(updatedTeacher.getGender());
        existing.setBirthDay(updatedTeacher.getBirthDay());
        existing.setExperienceYears(updatedTeacher.getExperienceYears());
        existing.setMajors(updatedTeacher.getMajors());
        existing.setRanks(updatedTeacher.getRanks());

        return teacherRepository.save(existing);
    }

    @Override
    public void deleteByCode(String code) {
        Teacher teacher = teacherRepository.findByCode(code);
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher not found with code: " + code);
        }
        teacherRepository.delete(teacher);
    }
}

