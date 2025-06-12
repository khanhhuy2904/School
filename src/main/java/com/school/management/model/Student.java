package com.school.management.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Student {
    private String nameStudent;
    private String codeStudent;
    private String gender;
    private LocalDate birthDay;
    private String major;

    private List<Subject> subjectList = new ArrayList<>();

    public List<Subject> getSubjectsList() {return subjectList;}

    public void addSubjects(Subject subject) {subjectList.add(subject);

    }

    public Student(String nameStudent, String gender, LocalDate birthDay, String major) {
        this.nameStudent = nameStudent;
        this.gender = gender;
        this.birthDay = birthDay;
        this.major = major;
        this.codeStudent = generateCodeStudent();
    }



    private String generateCodeStudent(){
        return "ST" + String.format("%05d", new Random().nextInt(100000));
    }

    public String getNameStudent() {
        return nameStudent;
    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }

    public String getCodeStudent() {
        return codeStudent;
    }

    public void setCodeStudent(String codeStudent) {
        this.codeStudent = codeStudent;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
    @Override
    public String toString(){
        return codeStudent  + " Name: "+ nameStudent+ ", " + " Gender:" + gender +", " + " BirthDay:" + birthDay+ ", " + " Major:" + major + "\n" ;
    };
}
