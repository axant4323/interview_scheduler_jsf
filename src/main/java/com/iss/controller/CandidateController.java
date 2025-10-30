package com.iss.controller;

import com.iss.model.Status;
import com.iss.repository.UserRepository;
import com.iss.security.SecurityUser;
import com.iss.service.InterviewService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/candidate")
public class CandidateController {
    private final InterviewService interviewService;
    private final UserRepository userRepository;

    public CandidateController(InterviewService interviewService, UserRepository userRepository) {
        this.interviewService = interviewService;
        this.userRepository = userRepository;
    }

    @GetMapping("/interviews")
    public String myInterviews(Authentication auth, Model model) {
        SecurityUser su = (SecurityUser) auth.getPrincipal();
        var user = userRepository.findByEmail(su.getUsername()).orElseThrow();
        model.addAttribute("interviews", interviewService.getForCandidate(user));
        return "candidate/interviews";
    }

    @PostMapping("/interviews/{id}/confirm")
    public String confirm(@PathVariable Long id) {
        interviewService.updateStatus(id, Status.Confirmed);
        return "redirect:/candidate/interviews";
    }

    @PostMapping("/interviews/{id}/decline")
    public String decline(@PathVariable Long id) {
        interviewService.updateStatus(id, Status.Declined);
        return "redirect:/candidate/interviews";
    }
}
