package com.school.management.view;

import com.school.management.controller.*;
import com.school.management.model.Student;
import com.school.management.model.Subject;
import com.school.management.model.Teacher;

import java.util.Scanner;

public class SubjectView {

    public static void printSubjectDetail(Subject subject) {
        System.out.println("=== Subject Detail ===");
        System.out.println("Code          : " + subject.getCodeSubject());
        System.out.println("Name          : " + subject.getNameSubject());
        System.out.println("Credit        : " + subject.getCredit());
        System.out.println("Max Students  : " + subject.getMaxStudent());

        if (subject.getTeacher() != null) {
            System.out.println("Teacher       : " +
                    subject.getTeacher().getCodeTeacher() + " - " +
                    subject.getTeacher().getNameTeacher());
        } else {
            System.out.println("Teacher       : (none)");
        }
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
}
