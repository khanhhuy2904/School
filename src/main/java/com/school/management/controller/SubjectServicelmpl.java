package com.school.management.controller;

import com.school.management.DBUtil;
import com.school.management.constant.Constants;
import com.school.management.exceptions.SubjectDuplicateException;
import com.school.management.exceptions.SubjectModificationException;
import com.school.management.exceptions.SubjectNotFoundException;
import com.school.management.exceptions.TeacherNotFoundException;
import com.school.management.model.Subject;
import com.school.management.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectServicelmpl implements SubjectService {
    @Override
    public List<Subject> getAll() {
        List<Subject> subjects = new ArrayList<>();

        String sql = "SELECT s.code, s.name, s.credit, s.max_students, t.name AS teacher_name " +
                "FROM subjects s " +
                "LEFT JOIN teachers t ON s.teacher_id = t.teacher_id";

        try (

                Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Subject subject = new Subject(
                        rs.getString("name"),
                        rs.getInt("max_students"),
                        rs.getInt("credit")
                );
                subject.setCodeSubject(rs.getString("code"));

                String teacherName = rs.getString("teacher_name");
                if (teacherName != null) {
                    Teacher teacher = new Teacher();
                    teacher.setNameTeacher(teacherName);
                    subject.setTeacher(teacher);
                }

                subjects.add(subject);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load subjects: " + e.getMessage(), e);
        }

        return subjects;
    }

    @Override
    public Subject getByCode(String code) {
        String sql = "SELECT s.code, s.name, s.credit, s.max_students, " +
                "t.code AS teacher_code, t.name AS teacher_name " +
                "FROM subjects s " +
                "LEFT JOIN teachers t ON s.teacher_id = t.teacher_id " +
                "WHERE s.code = ?";

        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new SubjectNotFoundException("Subject not found with code: " + code);
            }

            Subject subject = new Subject(
                    rs.getString("name"),
                    rs.getInt("max_students"),
                    rs.getInt("credit")
            );
            subject.setCodeSubject(rs.getString("code"));

            String teacherCode = rs.getString("teacher_code");
            String teacherName = rs.getString("teacher_name");
            if (teacherCode != null) {
                Teacher teacher = new Teacher();
                teacher.setCodeTeacher(teacherCode);
                teacher.setNameTeacher(teacherName);
                subject.setTeacher(teacher);
            }

            return subject;

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching subject: " + e.getMessage(), e);
        }
    }

    @Override
    public void create(Subject subject) {
        String sql = "INSERT INTO subjects (code, name, credit, max_students) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, subject.getCodeSubject());
            stmt.setString(2, subject.getNameSubject());
            stmt.setInt(3, subject.getCredit());
            stmt.setInt(4, subject.getMaxStudent());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Subject created successfully.");
            } else {
                System.out.println("Failed to create subject.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error creating subject: " + e.getMessage(), e);
        }
    }

    @Override
    public void edit(Subject subject) {
        try {
            // 1. Kiểm tra nếu môn học đã có giáo viên
            String checkTeacherSql = "SELECT teacher_id FROM subjects WHERE code = ? AND teacher_id IS NOT NULL";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement checkTeacherStmt = conn.prepareStatement(checkTeacherSql)) {
                checkTeacherStmt.setString(1, subject.getCodeSubject());
                ResultSet rs = checkTeacherStmt.executeQuery();
                if (rs.next()) {
                    throw new IllegalStateException("Cannot edit subject: already assigned to a teacher.");
                }
            }

            // 2. Kiểm tra nếu môn học đã có sinh viên
            String checkStudentSql = "SELECT COUNT(*) AS total FROM student_subjects ss " +
                    "JOIN subjects s ON ss.subject_id = s.subject_id " +
                    "WHERE s.code = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement checkStudentStmt = conn.prepareStatement(checkStudentSql)) {
                checkStudentStmt.setString(1, subject.getCodeSubject());
                ResultSet rs = checkStudentStmt.executeQuery();
                if (rs.next() && rs.getInt("total") > 0) {
                    throw new IllegalStateException("Cannot edit subject: students already enrolled.");
                }
            }

            // 3. Cập nhật
            String updateSql = "UPDATE subjects SET name = ?, credit = ?, max_students = ? WHERE code = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, subject.getNameSubject());
                updateStmt.setInt(2, subject.getCredit());
                updateStmt.setInt(3, subject.getMaxStudent());
                updateStmt.setString(4, subject.getCodeSubject());

                int rows = updateStmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Subject updated successfully.");
                } else {
                    System.out.println("No subject found to update.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating subject: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteByCode(String code) {
        try {
            // 1. Kiểm tra nếu môn học đã có giáo viên
            String checkTeacherSql = "SELECT teacher_id FROM subjects WHERE code = ? AND teacher_id IS NOT NULL";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement checkTeacherStmt = conn.prepareStatement(checkTeacherSql)) {
                checkTeacherStmt.setString(1, code);
                ResultSet rs = checkTeacherStmt.executeQuery();
                if (rs.next()) {
                    throw new SubjectModificationException("Cannot delete subject: already assigned to a teacher.");
                }
            }

            // 2. Kiểm tra nếu có sinh viên đang học
            String checkStudentSql = "SELECT COUNT(*) AS total FROM student_subjects ss " +
                    "JOIN subjects s ON ss.subject_id = s.subject_id " +
                    "WHERE s.code = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement checkStudentStmt = conn.prepareStatement(checkStudentSql)) {
                checkStudentStmt.setString(1, code);
                ResultSet rs = checkStudentStmt.executeQuery();
                if (rs.next() && rs.getInt("total") > 0) {
                    throw new SubjectModificationException("Cannot delete subject: students already enrolled.");
                }
            }

            // 3. Xóa môn học
            String deleteSql = "DELETE FROM subjects WHERE code = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setString(1, code);
                int rows = deleteStmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Subject deleted successfully.");
                } else {
                    System.out.println("Subject not found.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting subject: " + e.getMessage(), e);
        }
    }

    public void assignTeacherToSubject(String subjectCode, String teacherCode) {
        try {
            // 1. Lấy teacher_id
            String getTeacherIdSql = "SELECT teacher_id FROM teachers WHERE code = ?";
            long teacherId;
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(getTeacherIdSql)) {
                stmt.setString(1, teacherCode);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) throw new TeacherNotFoundException("Teacher not found.");
                teacherId = rs.getLong("teacher_id");
            }

            // 2. Kiểm tra số lượng môn giáo viên đang dạy
            String countSql = "SELECT COUNT(*) FROM subjects WHERE teacher_id = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(countSql)) {
                stmt.setLong(1, teacherId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) >= 3) {
                    throw new IllegalStateException("This teacher is already teaching 3 subjects.");
                }
            }

            // 3. Lấy subject_id
            String getSubjectIdSql = "SELECT subject_id FROM subjects WHERE code = ?";
            long subjectId;
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(getSubjectIdSql)) {
                stmt.setString(1, subjectCode);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) throw new SubjectNotFoundException("Subject not found.");
                subjectId = rs.getLong("subject_id");
            }

            // 4. Gán giáo viên cho môn học
            String updateSql = "UPDATE subjects SET teacher_id = ? WHERE subject_id = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setLong(1, teacherId);
                stmt.setLong(2, subjectId);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Teacher assigned to subject successfully.");
                } else {
                    System.out.println("Failed to assign teacher.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error assigning teacher: " + e.getMessage(), e);
        }
    }

    public void assignStudentsToSubject(String subjectCode, List<String> studentCodes) {
        try {
            // 1. Lấy subject_id, max_students, credit
            String subjectSql = "SELECT subject_id, max_students, credit FROM subjects WHERE code = ?";
            long subjectId;
            int maxStudents;
            int subjectCredit;
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(subjectSql)) {
                stmt.setString(1, subjectCode);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) throw new SubjectNotFoundException("Subject not found.");
                subjectId = rs.getLong("subject_id");
                maxStudents = rs.getInt("max_students");
                subjectCredit = rs.getInt("credit");
            }

            // 2. Kiểm tra số sinh viên hiện tại trong môn học
            String countSql = "SELECT COUNT(*) FROM student_subjects WHERE subject_id = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(countSql)) {
                stmt.setLong(1, subjectId);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                int currentCount = rs.getInt(1);

                if (currentCount + studentCodes.size() > maxStudents) {
                    throw new IllegalStateException("Cannot add students: Exceeds max allowed students for this subject.");
                }
            }

            // 3. Chuẩn bị thêm từng sinh viên
            String getStudentIdSql = "SELECT student_id FROM students WHERE code = ?";
            String totalCreditSql = "SELECT SUM(s.credit) FROM student_subjects ss JOIN subjects s ON ss.subject_id = s.subject_id WHERE ss.student_id = ?";
            String insertSql = "INSERT INTO student_subjects (student_id, subject_id) VALUES (?, ?)";

            try (   Connection conn = DBUtil.getConnection();
                    PreparedStatement getStudentIdStmt = conn.prepareStatement(getStudentIdSql);
                    PreparedStatement totalCreditStmt = conn.prepareStatement(totalCreditSql);
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql)
            ) {
                for (String studentCode : studentCodes) {
                    // 3.1 Lấy student_id
                    getStudentIdStmt.setString(1, studentCode);
                    ResultSet rs = getStudentIdStmt.executeQuery();
                    if (!rs.next()) {
                        System.out.println("Student not found: " + studentCode);
                        continue;
                    }
                    long studentId = rs.getLong("student_id");

                    // 3.2 Tính tổng tín chỉ đang học
                    totalCreditStmt.setLong(1, studentId);
                    ResultSet creditRs = totalCreditStmt.executeQuery();
                    creditRs.next();
                    int totalCredits = creditRs.getInt(1);

                    if (totalCredits + subjectCredit > 20) {
                        System.out.println("Student " + studentCode + " exceeds credit limit. Skipped.");
                        continue;
                    }

                    // 3.3 Chèn vào bảng liên kết
                    insertStmt.setLong(1, studentId);
                    insertStmt.setLong(2, subjectId);
                    insertStmt.addBatch();
                }

                insertStmt.executeBatch();
                System.out.println("Students added to subject successfully.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error assigning students: " + e.getMessage(), e);
        }
    }


}
