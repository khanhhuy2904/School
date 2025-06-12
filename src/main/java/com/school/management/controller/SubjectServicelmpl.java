package com.school.management.controller;

import com.school.management.constant.Constants;
import com.school.management.exceptions.SubjectDuplicateException;
import com.school.management.exceptions.SubjectModificationException;
import com.school.management.exceptions.SubjectNotFoundException;
import com.school.management.model.Subject;

import java.util.List;

public class SubjectServicelmpl implements SubjectService {
    @Override
    public List<Subject> getAll() {
        return Constants.globalSubjects;
    }

    @Override
    public Subject getByCode(String code) {
        for (Subject subject : Constants.globalSubjects) {
            if (subject.getCodeSubject().equalsIgnoreCase(code)) {
                return subject;
            }
        }
        throw new SubjectNotFoundException("Not found subject with code " + code);
    }

    @Override
    public void create(Subject subject) {
        for (Subject oldSubject : Constants.globalSubjects) {
            if (subject.getCodeSubject().equals(oldSubject.getCodeSubject())) {
                throw new SubjectDuplicateException("Subject code " + subject.getCodeSubject() + " exists");
            }
        }
        Constants.globalSubjects.add(subject);
    }

    @Override
    public void edit(Subject subject) {
        for (int i = 0; i < Constants.globalSubjects.size(); i++) {
            Subject current = Constants.globalSubjects.get(i);

            if (current.getCodeSubject().equalsIgnoreCase(subject.getCodeSubject())) {
                if (current.getTeacher() != null || current.getStudentList() != null ) {
                    throw new SubjectModificationException("Cannot edit this subject. It already has a teacher or students.");
                }

                Constants.globalSubjects.set(i, subject);
                break;
            }
        }
    }

    @Override
    public void deleteByCode(String code) {
        Subject subject = this.getByCode(code);

        if (subject.getTeacher() != null || subject.getStudentList() != null ) {
            throw new SubjectModificationException("Cannot delete this subject. It already has a teacher or students.");
        }

        Constants.globalSubjects.remove(subject);
    }
}
