package com.example.ResumeAnalyzerPro_Final.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ResumeAnalyzerPro_Final.repository.UserRepository;
import com.example.ResumeAnalyzerPro_Final.entity.User;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

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

        if (user.getActive() == null) {
            user.setActive(true);
        }

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
