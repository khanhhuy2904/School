package com.school.management.constant;

import com.school.management.model.Student;
import com.school.management.model.Subject;
import com.school.management.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public final static int SHOW_LIST_STUDENT = 1;
    public final static int GET_DETAIL_STUDENT = 2;
    public final static int CREATE_STUDENT = 3;
    public final static int EDIT_STUDENT = 4;
    public final static int DELETE_STUDENT = 5;
    public final static int SHOW_LIST_TEACHER = 6;
    public final static int GET_DETAIL_TEACHER = 7;
    public final static int CREATE_TEACHER = 8;
    public final static int EDIT_TEACHER = 9;
    public final static int DELETE_TEACHER = 10;
    public final static int SHOW_LIST_SUBJECT = 11;
    public final static int GET_DETAIL_SUBJECT = 12;
    public final static int CREATE_SUBJECT = 13;
    public final static int EDIT_SUBJECT = 14;
    public final static int DELETE_SUBJECT = 15;
    public static final int ASSIGN_TEACHER_TO_SUBJECT = 16;
    public static final int ADD_STUDENTS_TO_SUBJECT = 17;




    public static List<Teacher> globalTeachers = new ArrayList<>();
    public static List<Subject> globalSubjects = new ArrayList<>();



}
