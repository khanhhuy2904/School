package com.example.controller;

import com.example.entity.Major;
import com.example.entity.Rank;
import com.example.entity.Student;
import com.example.entity.Teacher;
import com.example.service.MajorService;
import com.example.service.RankService;
import com.example.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/teachers")

public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private MajorService majorService;
    @Autowired
    private RankService rankService;

    @GetMapping
    public String listTeachers(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "teachers/list-teacher";
    }

    @GetMapping("/new")
    public String showCreateTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("allMajors", majorService.findAll());
        model.addAttribute("allRanks", rankService.findAll());
        return "teachers/form-teacher";
    }

    @PostMapping("/save")
    public String saveTeacher(@ModelAttribute("teacher") @Valid Teacher teacher,
                              BindingResult bindingResult,
                              @RequestParam List<Long> selectedMajors,
                              @RequestParam List<Long> selectedRanks,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allMajors", majorService.findAll());
            model.addAttribute("allRanks", rankService.findAll());
            return "teachers/form-teacher";
        }

        List<Major> majors = majorService.findByMajorIdIn(selectedMajors);
        List<Rank> ranks = rankService.findByRankIdIn(selectedRanks);
        teacher.setMajors(majors);
        teacher.setRanks(ranks);

        teacherService.generateAndSaveTeacher(teacher);
        return "redirect:/teachers";
    }


    @GetMapping("/edit-by-code")
    public String showEditTeacherByCodeForm() {
        return "teachers/edit-by-code-form"; // form nhập code
    }

    @PostMapping("/edit-by-code")
    public String loadTeacherForEdit(@RequestParam("code") String code, Model model) {
        Teacher teacher = teacherService.findByCode(code);
        if (teacher == null) {
            model.addAttribute("error", "Teacher not found");
            return "teachers/edit-by-code-form";
        }

        model.addAttribute("teacher", teacher);
        model.addAttribute("majors", majorService.findAll());
        model.addAttribute("ranks", rankService.findAll());

        return "teachers/form-updateTeacher"; // hiển thị form sửa
    }

    @PostMapping("/update")
    public String updateTeacher(@ModelAttribute("teacher") @Valid Teacher teacher,
                                BindingResult bindingResult,
                                @RequestParam List<Long> selectedMajors,
                                @RequestParam List<Long> selectedRanks,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("majors", majorService.findAll());
            model.addAttribute("ranks", rankService.findAll());
            return "teachers/form-updateTeacher";
        }

        List<Major> majors = majorService.findByMajorIdIn(selectedMajors);
        List<Rank> ranks = rankService.findByRankIdIn(selectedRanks);
        teacher.setMajors(majors);
        teacher.setRanks(ranks);

        teacherService.update(teacher);
        return "redirect:/teachers";
    }


    @GetMapping("/delete-form")
    public String showDeleteForm() {
        return "teachers/delete-by-code-form";
    }

    @PostMapping("/delete-by-code")
    public String deleteTeacherByCode(@RequestParam("code") String code, Model model) {
        Teacher teacher = teacherService.findByCode(code);
        if (teacher == null) {
            model.addAttribute("error", "Teacher not found");
            return "teachers/delete-by-code-form";
        }

        teacherService.deleteByCode(code);
        return "redirect:/teachers";
    }
}
