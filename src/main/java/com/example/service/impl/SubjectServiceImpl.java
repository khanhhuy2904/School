package com.example.service.impl;

import com.example.entity.Subject;
import com.example.entity.Teacher;
import com.example.repository.SubjectRepository;
import com.example.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject generateAndSaveSubject(Subject subject) {
        long count = subjectRepository.count();
        String code = String.format("MH%05d", count + 1);
        subject.setCode(code);
        return subjectRepository.save(subject);
    }

    @Override
    public void deleteByCode(String code) {
        Subject subject = subjectRepository.findByCode(code);
        if (subject != null) {
            subjectRepository.delete(subject);
        }
    }

    @Override
    public Subject findByCode(String code) {
        return subjectRepository.findByCode(code);
    }

    @Override
    public Subject update(Subject updated) {
        Subject existing = subjectRepository.findByCode(updated.getCode());
        existing.setName(updated.getName());
        existing.setCredit(updated.getCredit());
        existing.setMaxStudents(updated.getMaxStudents());
        return subjectRepository.save(existing);
    }
}

