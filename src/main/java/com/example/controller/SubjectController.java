package com.example.controller;

import com.example.entity.Subject;
import com.example.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

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
    public String saveSubject(@ModelAttribute Subject subject) {
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
    public String updateSubject(@ModelAttribute Subject subject) {
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

}

