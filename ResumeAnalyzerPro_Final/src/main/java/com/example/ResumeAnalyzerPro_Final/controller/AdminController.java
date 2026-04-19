package com.example.ResumeAnalyzerPro_Final.controller;

import com.example.ResumeAnalyzerPro_Final.entity.Analysis;
import com.example.ResumeAnalyzerPro_Final.entity.User;
import com.example.ResumeAnalyzerPro_Final.service.ResumeService;
import com.example.ResumeAnalyzerPro_Final.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResumeService resumeService;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getRole());
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (isAdmin(session)) {
            return "redirect:/admin/dashboard";
        }
        return "admin-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        User user = userService.login(email, password);
        if (user == null || !"ADMIN".equals(user.getRole())) {
            model.addAttribute("error", "Invalid admin credentials");
            return "admin-login";
        }
        session.setAttribute("user", user);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        List<User> users = userService.getAllUsers();
        List<Analysis> analyses = resumeService.getAllAnalyses();
        
        model.addAttribute("users", users);
        model.addAttribute("analyses", analyses);
        model.addAttribute("admin", session.getAttribute("user"));
        return "admin-dashboard";
    }

    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        User admin = (User) session.getAttribute("user");
        if (!admin.getId().equals(id)) {
            userService.deleteUser(id);
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/user/{id}/toggle-lock")
    public String toggleLockUser(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        User admin = (User) session.getAttribute("user");
        if (!admin.getId().equals(id)) {
            userService.toggleUserLock(id);
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
