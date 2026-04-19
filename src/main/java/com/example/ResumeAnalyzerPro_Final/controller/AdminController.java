package com.example.ResumeAnalyzerPro_Final.controller;

import com.example.ResumeAnalyzerPro_Final.entity.Analysis;
import com.example.ResumeAnalyzerPro_Final.entity.Resume;
import com.example.ResumeAnalyzerPro_Final.entity.User;
import com.example.ResumeAnalyzerPro_Final.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String adminLoginPage(HttpSession session) {
        if (isAdmin(session)) {
            return "redirect:/admin/dashboard";
        }

        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }

        return "admin-login";
    }

    @PostMapping("/login")
    public String adminLogin(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session,
                             Model model) {
        if (!adminService.isAdminLoginValid(email, password)) {
            model.addAttribute("error", "Invalid admin credentials");
            return "admin-login";
        }

        session.removeAttribute("user");
        session.setAttribute("isAdmin", true);
        session.setAttribute("adminEmail", email.trim());
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("adminEmail", session.getAttribute("adminEmail"));
        return "admin-dashboard";
    }

    @GetMapping("/users/{id}")
    public String userDetails(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") int page,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        User user = adminService.getUserById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/admin/dashboard";
        }

        int safePage = Math.max(page, 0);
        int pageSize = 5;

        List<Resume> resumes = adminService.getUserResumes(user);
        Page<Analysis> analysesPage = adminService.getUserAnalyses(user, safePage, pageSize);

        model.addAttribute("selectedUser", user);
        model.addAttribute("resumes", resumes);
        model.addAttribute("resumeCount", adminService.getUserResumeCount(user));
        model.addAttribute("analysisCount", adminService.getUserAnalysisCount(user));
        model.addAttribute("analyses", analysesPage.getContent());
        model.addAttribute("currentPage", safePage);
        model.addAttribute("hasNextPage", analysesPage.hasNext());
        model.addAttribute("hasPreviousPage", analysesPage.hasPrevious());
        return "admin-user-details";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        boolean deleted = adminService.deleteUserAndRelatedData(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "User and related records deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found.");
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users/{id}/suspend")
    public String suspendUser(@PathVariable Long id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        boolean updated = adminService.suspendUser(id);
        if (updated) {
            redirectAttributes.addFlashAttribute("success", "User suspended successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found.");
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        boolean updated = adminService.activateUser(id);
        if (updated) {
            redirectAttributes.addFlashAttribute("success", "User activated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found.");
        }
        return "redirect:/admin/dashboard";
    }

    private boolean isAdmin(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("isAdmin"));
    }
}
