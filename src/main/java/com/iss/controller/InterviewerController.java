package com.iss.controller;

import com.iss.repository.UserRepository;
import com.iss.security.SecurityUser;
import com.iss.service.InterviewService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/interviewer")
public class InterviewerController {
    private final InterviewService interviewService;
    private final UserRepository userRepository;

    public InterviewerController(InterviewService interviewService, UserRepository userRepository) {
        this.interviewService = interviewService;
        this.userRepository = userRepository;
    }

    @GetMapping("/interviews")
    public String myInterviews(Authentication auth, Model model) {
        SecurityUser su = (SecurityUser) auth.getPrincipal();
        var user = userRepository.findByEmail(su.getUsername()).orElseThrow();
        model.addAttribute("interviews", interviewService.getForInterviewer(user));
        return "interviewer/interviews";
    }
}
