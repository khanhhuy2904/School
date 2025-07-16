package com.example.controller;

import com.example.entity.Student;
import com.example.entity.Subject;
import com.example.entity.Teacher;
import com.example.service.StudentService;
import com.example.service.SubjectService;
import com.example.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String listSubjects(Model model) {
        model.addAttribute("subjects", subjectService.findAll());
        return "subjects/list-subjects";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("subject", new Subject());
        return "subjects/form-subject";
    }

    @PostMapping("/save")
    public String saveSubject(@ModelAttribute("subject") @Valid Subject subject,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "subjects/form-subject";
        }
        subjectService.generateAndSaveSubject(subject);
        return "redirect:/subjects";
    }


    @GetMapping("/edit-by-code")
    public String showEditByCodeForm() {
        return "subjects/edit-by-code-form";
    }

    @PostMapping("/edit-by-code")
    public String loadEditForm(@RequestParam("code") String code, Model model) {
        Subject subject = subjectService.findByCode(code);
        if (subject == null) {
            model.addAttribute("error", "Subject not found");
            return "subjects/edit-by-code-form";
        }
        model.addAttribute("subject", subject);
        return "subjects/form-updateSubject";
    }

    @PostMapping("/update")
    public String updateSubject(@ModelAttribute("subject") @Valid Subject subject,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "subjects/form-updateSubject";
        }
        subjectService.update(subject);
        return "redirect:/subjects";
    }


    @GetMapping("/delete-form")
    public String showDeleteForm() {
        return "subjects/delete-by-code-form";
    }

    @PostMapping("/delete-by-code")
    public String deleteSubjectByCode(@RequestParam("code") String code, Model model) {
        Subject subject = subjectService.findByCode(code);
        if (subject == null) {
            model.addAttribute("error", "Subject with code '" + code + "' not found.");
            return "subjects/delete-subject-form";
        }

        if (subject.getTeacher() != null || !subject.getStudents().isEmpty()) {
            model.addAttribute("error", "Cannot delete subject. It already has a teacher or students enrolled.");
            return "subjects/delete-subject-form";
        }

        subjectService.deleteByCode(code);
        return "redirect:/subjects";
    }

    @GetMapping("/add-teacher-form")
    public String showAddTeacherForm() {
        return "subjects/add-teacher-form";
    }

    @PostMapping("/add-teacher")
    public String addTeacherToSubject(@RequestParam("teacherCode") String teacherCode,
                                      @RequestParam("subjectCode") String subjectCode,
                                      Model model) {

        Teacher teacher = teacherService.findByCode(teacherCode);
        Subject subject = subjectService.findByCode(subjectCode);

        if (teacher == null) {
            model.addAttribute("error", "Teacher with code '" + teacherCode + "' not found.");
            return "subjects/add-teacher-form";
        }

        if (subject == null) {
            model.addAttribute("error", "Subject with code '" + subjectCode + "' not found.");
            return "subjects/add-teacher-form";
        }

        if (teacher.getSubjects().size() >= 3) {
            model.addAttribute("error", "Teacher has already been assigned to 3 subjects.");
            return "subjects/add-teacher-form";
        }

        if (subject.getTeacher() != null) {
            model.addAttribute("error", "This subject already has a teacher.");
            return "subjects/add-teacher-form";
        }

        subject.setTeacher(teacher);
        subjectService.update(subject);

        model.addAttribute("success", "Teacher assigned to subject successfully!");
        return "redirect:/subjects";
    }

    @GetMapping("/add-students")
    public String showAddStudentsForm() {
        return "subjects/add-students-form";
    }

    @PostMapping("/add-students")
    public String addStudentsToSubject(@RequestParam String subjectCode,
                                       @RequestParam String studentCodes,
                                       Model model) {
        Subject subject = subjectService.findByCode(subjectCode);
        if (subject == null) {
            model.addAttribute("error", "Subject not found.");
            return "subjects/add-students-form";
        }

        String[] codeArray = studentCodes.split(",");
        List<Student> foundStudents = new ArrayList<>();

        for (String code : codeArray) {
            Student student = studentService.findByCode(code.trim());
            if (student == null) {
                model.addAttribute("error", "Student with code '" + code.trim() + "' not found.");
                return "subjects/add-students-form";
            }

            int totalCredits = student.getSubjects().stream().mapToInt(Subject::getCredit).sum();
            if (totalCredits + subject.getCredit() > 20) {
                model.addAttribute("error", "Student '" + code.trim() + "' exceeds the 20-credit limit.");
                return "subjects/add-students-form";
            }

            foundStudents.add(student);
        }

        if (subject.getStudents().size() + foundStudents.size() > subject.getMaxStudents()) {
            model.addAttribute("error", "This subject cannot have more students.");
            return "subjects/add-students-form";
        }

        for (Student student : foundStudents) {
            subject.getStudents().add(student);
            student.getSubjects().add(subject);
        }
        subjectService.save(subject);

        List<Student> enrolledStudents = new ArrayList<>(subject.getStudents());

        model.addAttribute("subject", subject);
        model.addAttribute("enrolledStudents", enrolledStudents);

        return "subjects/subject-student-result";
    }

}

