package com.iss.controller;

import com.iss.security.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Authentication auth) {
        if (auth == null) return "redirect:/login";
        Object principal = auth.getPrincipal();
        if (principal instanceof SecurityUser su) {
            if (su.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin/interviews";
            }
            if (su.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
                return "redirect:/candidate/interviews";
            }
            if (su.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_INTERVIEWER"))) {
                return "redirect:/interviewer/interviews";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() { return "login"; }
}
