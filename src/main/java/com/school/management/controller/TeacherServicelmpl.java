package com.school.management.controller;

import com.school.management.constant.Constants;
import com.school.management.exceptions.TeacherDuplicateException;
import com.school.management.exceptions.TeacherNotFoundException;
import com.school.management.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherServicelmpl implements TeacherService{
    private Connection connection;
    public TeacherServicelmpl() {
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
    public List<Teacher> getAll() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT code, name, gender, birth_date, experience_years FROM teachers";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Teacher teacher = new Teacher(
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getDate("birth_date").toLocalDate(),
                        rs.getInt("experience_years")
                );
                teacher.setCodeTeacher(rs.getString("code"));
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load teachers: " + e.getMessage(), e);
        }

        return teachers;
    }


    @Override
    public Teacher getByCode(String code) {
        String sql = "SELECT t.code, t.name, t.gender, t.birth_date, t.experience_years, " +
                "r.rank_name, m.name AS major_name " +
                "FROM teachers t " +
                "LEFT JOIN teacher_rank tr ON t.teacher_id = tr.teacher_id " +
                "LEFT JOIN ranks r ON tr.rank_id = r.rank_id " +
                "LEFT JOIN teacher_major tm ON t.teacher_id = tm.teacher_id " +
                "LEFT JOIN majors m ON tm.major_id = m.major_id " +
                "WHERE t.code = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();

            Teacher teacher = null;
            List<String> majors = new ArrayList<>();
            List<String> ranks = new ArrayList<>();

            while (rs.next()) {
                if (teacher == null) {
                    teacher = new Teacher(
                            rs.getString("name"),
                            rs.getString("gender"),
                            rs.getDate("birth_date").toLocalDate(),
                            rs.getInt("experience_years")
                    );
                    teacher.setCodeTeacher(rs.getString("code"));
                }

                String major = rs.getString("major_name");
                if (major != null && !majors.contains(major)) {
                    majors.add(major);
                }

                String rank = rs.getString("rank_name");
                if (rank != null && !ranks.contains(rank)) {
                    ranks.add(rank);
                }
            }

            if (teacher == null) {
                throw new TeacherNotFoundException("Teacher not found with code: " + code);
            }

            teacher.setMajor(majors);
            teacher.setLevel(ranks);
            return teacher;

        } catch (SQLException e) {
            throw new RuntimeException("Error loading teacher: " + e.getMessage(), e);
        }
    }

    @Override
    public void create(Teacher teacher) {
        String insertTeacherSql = "INSERT INTO teachers (code, name, gender, birth_date, experience_years) VALUES (?, ?, ?, ?, ?)";

        try (
                PreparedStatement insertTeacherStmt = connection.prepareStatement(insertTeacherSql, Statement.RETURN_GENERATED_KEYS)
        ) {
            // 1. Insert teacher
            insertTeacherStmt.setString(1, teacher.getCodeTeacher());
            insertTeacherStmt.setString(2, teacher.getNameTeacher());
            insertTeacherStmt.setString(3, teacher.getGender());
            insertTeacherStmt.setDate(4, Date.valueOf(teacher.getBirthDay()));
            insertTeacherStmt.setInt(5, teacher.getExperienceYear());
            insertTeacherStmt.executeUpdate();

            // 2. Get generated teacher_id
            ResultSet generatedKeys = insertTeacherStmt.getGeneratedKeys();
            long teacherId;
            if (generatedKeys.next()) {
                teacherId = generatedKeys.getLong(1);
            } else {
                throw new SQLException("Failed to retrieve teacher_id.");
            }

            // 3. Insert teacher_majors
            String insertMajorSql = "INSERT INTO teacher_major (teacher_id, major_id) VALUES (?, ?)";
            try (PreparedStatement majorStmt = connection.prepareStatement(insertMajorSql)) {
                for (String majorName : teacher.getMajor()) {
                    int majorId = getMajorIdByName(majorName, connection);
                    majorStmt.setLong(1, teacherId);
                    majorStmt.setInt(2, majorId);
                    majorStmt.addBatch();
                }
                majorStmt.executeBatch();
            }

            // 4. Insert teacher_ranks
            String insertRankSql = "INSERT INTO teacher_rank (teacher_id, rank_id) VALUES (?, ?)";
            try (PreparedStatement rankStmt = connection.prepareStatement(insertRankSql)) {
                for (String rankName : teacher.getLevel()) {
                    int rankId = getRankIdByName(rankName, connection);
                    rankStmt.setLong(1, teacherId);
                    rankStmt.setInt(2, rankId);
                    rankStmt.addBatch();
                }
                rankStmt.executeBatch();
            }

            System.out.println("Teacher created successfully!");

        } catch (SQLException e) {
            throw new RuntimeException("Error creating teacher: " + e.getMessage(), e);
        }
    }
    private int getMajorIdByName(String name, Connection conn) throws SQLException {
        String sql = "SELECT major_id FROM majors WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("major_id");
            throw new SQLException("Major not found: " + name);
        }
    }

    private int getRankIdByName(String name, Connection conn) throws SQLException {
        String sql = "SELECT rank_id FROM ranks WHERE rank_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("rank_id");
            throw new SQLException("Rank not found: " + name);
        }
    }

    @Override
    public void edit(Teacher teacher) {
        String updateSql = "UPDATE teachers SET name = ?, gender = ?, birth_date = ?, experience_years = ? WHERE code = ?";
        String selectIdSql = "SELECT teacher_id FROM teachers WHERE code = ?";

        try (
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                PreparedStatement idStmt = connection.prepareStatement(selectIdSql)
        ) {
            // 1. Lấy teacher_id từ code
            idStmt.setString(1, teacher.getCodeTeacher());
            ResultSet rs = idStmt.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Teacher not found with code: " + teacher.getCodeTeacher());
            }
            long teacherId = rs.getLong("teacher_id");

            // 2. Cập nhật bảng teachers
            updateStmt.setString(1, teacher.getNameTeacher());
            updateStmt.setString(2, teacher.getGender());
            updateStmt.setDate(3, Date.valueOf(teacher.getBirthDay()));
            updateStmt.setInt(4, teacher.getExperienceYear());
            updateStmt.setString(5, teacher.getCodeTeacher());
            updateStmt.executeUpdate();

            // 3. Xóa majors/ranks cũ
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("DELETE FROM teacher_major WHERE teacher_id = " + teacherId);
                stmt.executeUpdate("DELETE FROM teacher_rank WHERE teacher_id = " + teacherId);
            }

            // 4. Chèn majors mới
            String insertMajorSql = "INSERT INTO teacher_major (teacher_id, major_id) VALUES (?, ?)";
            try (PreparedStatement majorStmt = connection.prepareStatement(insertMajorSql)) {
                for (String majorName : teacher.getMajor()) {
                    int majorId = getMajorIdByName(majorName, connection);
                    majorStmt.setLong(1, teacherId);
                    majorStmt.setInt(2, majorId);
                    majorStmt.addBatch();
                }
                majorStmt.executeBatch();
            }

            // 5. Chèn ranks mới
            String insertRankSql = "INSERT INTO teacher_rank (teacher_id, rank_id) VALUES (?, ?)";
            try (PreparedStatement rankStmt = connection.prepareStatement(insertRankSql)) {
                for (String rankName : teacher.getLevel()) {
                    int rankId = getRankIdByName(rankName, connection);
                    rankStmt.setLong(1, teacherId);
                    rankStmt.setInt(2, rankId);
                    rankStmt.addBatch();
                }
                rankStmt.executeBatch();
            }

            System.out.println("Teacher updated successfully!");

        } catch (SQLException e) {
            throw new RuntimeException("Error updating teacher: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteByCode(String code) {
        String sql = "DELETE FROM teachers WHERE code = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, code);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Teacher deleted successfully.");
            } else {
                System.out.println("No teacher found with code: " + code);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting teacher: " + e.getMessage(), e);
        }
    }
}
