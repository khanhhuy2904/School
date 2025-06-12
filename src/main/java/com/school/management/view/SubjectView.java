package com.school.management.view;

import com.school.management.controller.*;
import com.school.management.model.Student;
import com.school.management.model.Subject;
import com.school.management.model.Teacher;

import java.util.Scanner;

public class SubjectView {
    private final SubjectService subjectService = new SubjectServicelmpl();
    private final TeacherService teacherService = new TeacherServicelmpl();
    private final StudentService studentService = new StudentServiceImpl();
    private final Scanner sc = new Scanner(System.in);


    public static void printSubjectDetail(Subject subject) {
        System.out.println("=== Subject Details ===");
        System.out.println("Subject Code       : " + subject.getCodeSubject());
        System.out.println("Subject Name       : " + subject.getNameSubject());
        System.out.println("Credits            : " + subject.getCredit());
        System.out.println("Maximum Students   : " + subject.getMaxStudent());
    }

    public static Subject screenCreateSubject() {
        System.out.println("=== Create New Subject ===");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter subject name: ");
        String name = sc.nextLine();

        int credit;
        while (true) {
            System.out.print("Enter number of credits: ");
            try {
                credit = Integer.parseInt(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter an integer.");
            }
        }

        int maxStudents;
        while (true) {
            System.out.print("Enter maximum number of students: ");
            try {
                maxStudents = Integer.parseInt(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter an integer.");
            }
        }

        Subject subject = new Subject(name, maxStudents, credit);
        System.out.println("Subject created successfully: " + subject.getCodeSubject());
        return subject;
    }

    public static Subject screenEditSubject(Subject subject) {

        System.out.println("=== Edit Subject Information ===");
        System.out.println("Current Subject Code       : " + subject.getCodeSubject());
        System.out.println("Current Subject Name       : " + subject.getNameSubject());
        System.out.println("Current Number of Credits  : " + subject.getCredit());
        System.out.println("Current Max Students       : " + subject.getMaxStudent());
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter new subject name: ");
        String name = sc.nextLine();
        if (!name.isEmpty()) {
            subject.setNameSubject(name);
        }

        System.out.print("Enter new number of credits: ");
        String creditStr = sc.nextLine();
        if (!creditStr.isEmpty()) {
            try {
                int credit = Integer.parseInt(creditStr);
                subject.setCredit(credit);
            } catch (NumberFormatException e) {
                System.out.println("Invalid credit number. Keeping current value.");
            }
        }

        System.out.print("Enter new max number of students: ");
        String maxStr = sc.nextLine();
        if (!maxStr.isEmpty()) {
            try {
                int maxStudents = Integer.parseInt(maxStr);
                subject.setMaxStudent(maxStudents);
            } catch (NumberFormatException e) {
                System.out.println("Invalid student number. Keeping current value.");
            }
        }

        System.out.println("Subject updated successfully.");
        return subject;
    }

    public void assignTeacherToSubject() {
        System.out.println("=== Assign Teacher to Subject ===");

        System.out.print("Enter subject code: ");
        String subjectCode = sc.nextLine();

        Subject subject;
        try {
            subject = subjectService.getByCode(subjectCode);
        } catch (Exception e) {
            System.out.println("Subject not found.");
            return;
        }


        if (subject.getTeacher() != null) {
            System.out.println("This subject already has a teacher: " + subject.getTeacher().getNameTeacher());
            return;
        }

        System.out.print("Enter teacher code: ");
        String teacherCode = sc.nextLine();

        Teacher teacher;
        try {
            teacher = teacherService.getByCode(teacherCode);
        } catch (Exception e) {
            System.out.println("Teacher not found.");
            return;
        }

        if(teacher.getSubjectsList().size()>=3){
            System.out.println("This teacher is already teaching 3 subjects.");
            return;
        }

        subject.setTeacher(teacher);
        teacher.addSubjects(subject);
        System.out.println("Assigned teacher " + teacher.getNameTeacher() + " to subject " + subject.getNameSubject());
    }

    public void addStudentsToSubject() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Add Students to Subject ===");

        System.out.print("Enter subject code: ");
        String subjectCode = sc.nextLine();

        Subject subject;
        try {
            subject = subjectService.getByCode(subjectCode);
        } catch (Exception e) {
            System.out.println("Subject not found.");
            return;
        }

        while (true) {
            if (subject.getStudentList().size() >= subject.getMaxStudent()) {
                System.out.println("This subject has reached its maximum number of students (" + subject.getMaxStudent() + ")");
                break;
            }

            System.out.print("Enter student code to add (or type 'x' to stop): ");
            String studentCode = sc.nextLine();
            if (studentCode.equalsIgnoreCase("x")) break;

            Student student;
            try {
                student = studentService.getByCode(studentCode);
            } catch (Exception e) {
                System.out.println("Student not found.");
                continue;
            }


            if (subject.getStudentList().contains(student)) {
                System.out.println("Student is already enrolled in this subject.");
                continue;
            }


            int totalCredits = 0;
            for(Subject countcredit : student.getSubjectsList()){
                totalCredits += countcredit.getCredit();
            }

            if(totalCredits >= 20){
                System.out.println("Cannot add student. Credit limit exceeded (currently: " + totalCredits + "/20)");
                continue;
            }

            // Add student
            subject.addStudents(student);
            student.addSubjects(subject);
            System.out.println("Student " + student.getNameStudent() + " added to subject " + subject.getNameSubject());
        }

        subjectService.edit(subject); // Save updated subject
    }

}
