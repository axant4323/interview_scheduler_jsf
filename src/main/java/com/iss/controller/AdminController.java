package com.iss.controller;

import com.iss.model.Status;
import com.iss.model.User;
import com.iss.repository.UserRepository;
import com.iss.service.InterviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final InterviewService interviewService;
    private final UserRepository userRepository;

    public AdminController(InterviewService interviewService, UserRepository userRepository) {
        this.interviewService = interviewService;
        this.userRepository = userRepository;
    }

    @GetMapping("/interviews")
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/interviews/list";
    }

    @PostMapping("/interviews")
    public String create(@RequestParam Long candidateId,
                         @RequestParam Long interviewerId,
                         @RequestParam String date,
                         @RequestParam String time,
                         Model model) {
        User candidate = userRepository.findById(candidateId).orElseThrow();
        User interviewer = userRepository.findById(interviewerId).orElseThrow();
        try {
            interviewService.schedule(candidate, interviewer, LocalDate.parse(date), LocalTime.parse(time));
            return "redirect:/admin/interviews";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("users", userRepository.findAll());
            return "admin/interviews/list";
        }
    }

    @PostMapping("/interviews/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam Status status) {
        interviewService.updateStatus(id, status);
        return "redirect:/admin/interviews";
    }
}
