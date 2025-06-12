package com.school.management;

import com.school.management.model.Student;
import com.school.management.view.MenuView;

import java.sql.*;


public class SchoolApplication {

    public static void main(String[] args) {
        MenuView view = new MenuView();
        view.printMenu();
    }
}