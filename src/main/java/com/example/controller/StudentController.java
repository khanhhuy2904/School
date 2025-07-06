package com.example.controller;

import com.example.entity.Student;
import com.example.service.MajorService;
import com.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private MajorService majorService;


    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "students/list-student";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("majors", majorService.findAll()); // Truyền vào dropdown
        return "students/form-student";
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.generateAndSaveStudent(student);
        return "redirect:/students";
    }

        @GetMapping("/edit-by-code")
        public String showEditByCodeForm() {
            return "students/edit-by-code-form";
        }


        @PostMapping("/edit-by-code")
        public String loadStudentForEdit(@RequestParam("code") String code, Model model) {
            Student student = studentService.findByCode(code);
            if (student == null) {
                model.addAttribute("error", "Student not found");
                return "students/edit-by-code-form";
            }
            model.addAttribute("student", student);
            model.addAttribute("majors", majorService.findAll());
            return "students/form-updateStudent";
        }


        @PostMapping("/update")
        public String updateStudent(@ModelAttribute("student") Student student) {
            studentService.update(student);
            return "redirect:/students";
    }

    @GetMapping("/delete-form")
    public String showDeleteForm() {
        return "students/delete-student-form"; // Trang chứa ô nhập code
    }

    @PostMapping("/delete-by-code")
    public String deleteStudentByCode(@RequestParam("code") String code, Model model) {
        Student student = studentService.findByCode(code);
        if (student == null) {
            model.addAttribute("error", "Student with code '" + code + "' not found.");
            return "students/delete-student-form";
        }

        studentService.deleteByCode(code);
        return "redirect:/students";
    }

}
