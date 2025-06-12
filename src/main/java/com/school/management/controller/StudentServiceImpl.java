package com.school.management.controller;

import com.school.management.constant.Constants;
import com.school.management.exceptions.StudentDuplicateException;
import com.school.management.exceptions.StudentNotFoundException;
import com.school.management.model.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentServiceImpl implements StudentService {
    private Connection connection;
    public StudentServiceImpl() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_management", "root", "Huynguyen29");
        } catch (SQLException e) {
            throw new RuntimeException("Connection failed: " + e.getMessage());
        }
//        } finally {
//            try {
//                if (connection != null && !connection.isClosed()) {
//                    connection.close();
//                }
//            } catch (Exception ex) {
//                System.out.println("⚠ Error closing connection: " + ex.getMessage());
//            }
//        }
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.code, s.name, s.gender, s.birth_date, m.name AS major_name\n" +
                "        FROM students s\n" +
                "        LEFT JOIN majors m ON s.major_id = m.major_id";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
                String major = rs.getString("major_name");

                Student student = new Student(name, gender, birthDate, major);
                student.setCodeStudent(code);
                students.add(student);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch students: " + e.getMessage());
        }

        return students;
    }

    @Override
    public Student getByCode(String code) {
        String sql = "SELECT s.code, s.name, s.gender, s.birth_date, m.name AS major_name " +
                "FROM students s " +
                "LEFT JOIN majors m ON s.major_id = m.major_id " +
                "WHERE s.code = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)

        ) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Student student = new Student(
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getDate("birth_date").toLocalDate(),
                        rs.getString("major_name")
                );
                student.setCodeStudent(rs.getString("code"));
                return student;
            } else {
                throw new StudentNotFoundException("Student not found with code: " + code);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching student: " + e.getMessage(), e);
        }
    }

    @Override
    public void create(Student student) {
        String sql = "INSERT INTO students (code, name, gender, birth_date, major_id) VALUES (?, ?, ?, ?, ?)";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, student.getCodeStudent());
            stmt.setString(2, student.getNameStudent());
            stmt.setString(3, student.getGender());
            stmt.setDate(4, Date.valueOf(student.getBirthDay()));
            stmt.setInt(5, getMajorIdByName(student.getMajor(), connection));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student created successfully!");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting student: " + e.getMessage(), e);
        }
    }
    private int getMajorIdByName(String majorName, Connection conn) throws SQLException {
        String sql = "SELECT major_id FROM majors WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, majorName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("major_id");
            } else {
                throw new SQLException("Major not found: " + majorName);
            }
        }
    }

    @Override
    public void edit(Student student) {
        String sql = "UPDATE students SET name = ?, gender = ?, birth_date = ?, major_id = ? WHERE code = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, student.getNameStudent());
            stmt.setString(2, student.getGender());
            stmt.setDate(3, Date.valueOf(student.getBirthDay()));
            stmt.setInt(4, getMajorIdByName(student.getMajor(), connection));
            stmt.setString(5, student.getCodeStudent());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student updated successfully.");
            } else {
               throw new StudentNotFoundException("Student not found with code: " + student.getCodeStudent());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating student: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteByCode(String code) {
        String sql = "DELETE FROM students WHERE code = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, code);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("No student found with code: " + code);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting student: " + e.getMessage(), e);
        }
    }
}
