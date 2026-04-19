package com.example.ResumeAnalyzerPro_Final.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.List;

import com.example.ResumeAnalyzerPro_Final.repository.UserRepository;
import com.example.ResumeAnalyzerPro_Final.repository.AnalysisRepository;
import com.example.ResumeAnalyzerPro_Final.repository.ResumeRepository;
import com.example.ResumeAnalyzerPro_Final.entity.User;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private AnalysisRepository analysisRepo;

    @Autowired
    private ResumeRepository resumeRepo;

    @PostConstruct
    public void initAdmin() {
        if (repo.findByEmail("admin@admin.com") == null) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword("admin123");
            admin.setRole("ADMIN");
            repo.save(admin);
        }
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = repo.findById(id).orElse(null);
        if (user != null) {
            analysisRepo.deleteByUser(user);
            resumeRepo.deleteByUser(user);
            repo.delete(user);
        }
    }

    @Transactional
    public void toggleUserLock(Long id) {
        User user = repo.findById(id).orElse(null);
        if (user != null) {
            user.setLocked(!user.isLocked());
            repo.save(user); // Triggers update
        }
    }

    public User register(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new RuntimeException("Name is required.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("Email is required.");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Password is required.");
        }

        User existingUser = repo.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new RuntimeException("An account with this email already exists.");
        }

        user.setRole("USER");
        return repo.save(user);
    }

    public User login(String email, String password) {
        User user = repo.findByEmail(email);
        if (user == null) {
            return null;
        }
        if (user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
