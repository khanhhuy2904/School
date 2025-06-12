package com.school.management.view;

import com.school.management.model.Student;

import java.time.LocalDate;
import java.util.Scanner;

public class StudentView {

    public static Student screenCreateStudent() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter gender: ");
        String gender = sc.nextLine();

        LocalDate birthday = null;
        while (birthday == null) {
            System.out.print("Enter birthday (yyyy-mm-dd): ");
            String birthdayStr = sc.nextLine();
            try {
                birthday = LocalDate.parse(birthdayStr);
            } catch (Exception ex) {
                System.out.println("Invalid date format");
            }
        }
        System.out.print("Enter major: ");
        String major = sc.nextLine();

        Student student = new Student(name, gender, birthday, major);
        return student;
    }

    public void printStudentDetail(Student student) {
        System.out.println("==== Student Detail ====");
        System.out.println("Code        : " + student.getCodeStudent());
        System.out.println("Name        : " + student.getNameStudent());
        System.out.println("Gender      : " + student.getGender());
        System.out.println("Birthday    : " + student.getBirthDay());
        System.out.println("Major       : " + student.getMajor());
    }

    public static Student screenEditStudent(Student student) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter new name (" + student.getNameStudent() + "): ");
        String name = sc.nextLine();
        if (!name.isEmpty()) student.setNameStudent(name);

        System.out.print("Enter new gender (" + student.getGender() + "): ");
        String gender = sc.nextLine();
        if (!gender.isEmpty()) student.setGender(gender);

        LocalDate birthday = null;
        while (birthday == null) {
            System.out.print("Enter new birthday (" + student.getBirthDay() + ") [yyyy-mm-dd]: ");
            String birthdayStr = sc.nextLine();
            if (!birthdayStr.isEmpty()) {
                try {
                    birthday = LocalDate.parse(birthdayStr);
                    student.setBirthDay(birthday);
                } catch (Exception ex) {
                    System.out.println("Invalid date format.");
                }
            }
        }

        System.out.print("Enter new major (" + student.getMajor() + "): ");
        String major = sc.nextLine();
        if (!major.isEmpty()) student.setMajor(major);

        return student;
    }
}

