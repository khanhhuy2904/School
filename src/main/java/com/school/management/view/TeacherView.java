package com.school.management.view;

import com.school.management.model.Teacher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeacherView {
    public static Teacher screenCreateTeacher() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter gender: ");
        String gender = sc.nextLine();

        // Birthday input with format checking
        LocalDate birthday = null;
        while (birthday == null) {
            System.out.print("Enter birthday (yyyy-MM-dd): ");
            String birthdayStr = sc.nextLine();
            try {
                birthday = LocalDate.parse(birthdayStr);
            } catch (Exception ex) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        // Experience years
        int experienceYear = -1;
        while (experienceYear < 0) {
            System.out.print("Enter years of experience: ");
            try {
                experienceYear = Integer.parseInt(sc.nextLine());
                if (experienceYear < 0) {
                    System.out.println("Experience must be a non-negative number.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }

        Teacher teacher = new Teacher(name, gender, birthday, experienceYear);
        System.out.println("Enter majors (type 'x' to stop):");
        while (true) {
            System.out.print("→ Enter major: ");
            String major = sc.nextLine();
            if (major.equalsIgnoreCase("x")) break;
            teacher.addMajor(major);
        }

        System.out.println("Enter levels (type 'x' to stop):");
        while (true) {
            System.out.print("→ Enter level: ");
            String level = sc.nextLine();
            if (level.equalsIgnoreCase("x")) break;
            teacher.addLevel(level);

        }
        return teacher;
    }

    public void printTeacherDetail(Teacher teacher) {
        System.out.println("==== Teacher Detail ====");
        System.out.println("Code           : " + teacher.getCodeTeacher());
        System.out.println("Name           : " + teacher.getNameTeacher());
        System.out.println("Gender         : " + teacher.getGender());
        System.out.println("Birthday       : " + teacher.getBirthDay());
        System.out.println("Experience (yrs): " + teacher.getExperienceYear());


        System.out.println("Majors         :");
        if (teacher.getMajor() != null && !teacher.getMajor().isEmpty()) {
            for (String m : teacher.getMajor()) {
                System.out.println("  - " + m);
            }
        } else {
            System.out.println("  (none)");
        }

        // Print levels
        System.out.println("Levels         :");
        if (teacher.getLevel() != null && !teacher.getLevel().isEmpty()) {
            for (String l : teacher.getLevel()) {
                System.out.println("  - " + l);
            }
        } else {
            System.out.println("  (none)");
        }
    }

    public static Teacher screenEditTeacher(Teacher teacher) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Leave input empty if you don't want to change the field.");

        System.out.print("Enter new name (" + teacher.getNameTeacher() + "): ");
        String name = sc.nextLine();
        if (!name.isEmpty()) teacher.setNameTeacher(name);

        System.out.print("Enter new gender (" + teacher.getGender() + "): ");
        String gender = sc.nextLine();
        if (!gender.isEmpty()) teacher.setGender(gender);


        LocalDate birthday = null;
        while (birthday == null) {
            System.out.print("Enter new birthday (" + teacher.getBirthDay() + ") [yyyy-MM-dd]: ");
            String birthdayStr = sc.nextLine();
            try {
                birthday = LocalDate.parse(birthdayStr);
                teacher.setBirthDay(birthday);
            } catch (Exception ex) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        System.out.print("Enter new experience year (" + teacher.getExperienceYear() + "): ");
        int experienceYear = -1;
        while (experienceYear < 0) {
            System.out.print("Enter years of experience: ");
            try {
                experienceYear = Integer.parseInt(sc.nextLine());
                teacher.setExperienceYear(experienceYear);
                if (experienceYear < 0) {
                    System.out.println("Experience must be a non-negative number.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }

        // Update majors
        System.out.println("Update majors? (Y/N): ");
        String updateMajor = sc.nextLine();
        if (updateMajor.equalsIgnoreCase("Y")) {
            List<String> majors = new ArrayList<>();
            System.out.println("Enter majors (type 'x' to stop):");
            while (true) {
                System.out.print("→ Enter major: ");
                String major = sc.nextLine();
                if (major.equalsIgnoreCase("x")) break;
                majors.add(major);
            }
            teacher.setMajor(majors);
        }

        // Update levels
        System.out.println("Update levels? (Y/N): ");
        String updateLevel = sc.nextLine();
        if (updateLevel.equalsIgnoreCase("Y")) {
            List<String> levels = new ArrayList<>();
            System.out.println("Enter levels (type 'x' to stop):");
            while (true) {
                System.out.print("→ Enter level: ");
                String level = sc.nextLine();
                if (level.equalsIgnoreCase("x")) break;
                levels.add(level);
            }
            teacher.setLevel(levels);
        }
        return teacher;
    }
}
