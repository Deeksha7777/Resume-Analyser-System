package com.example.ResumeAnalyzerPro_Final.service;

import com.example.ResumeAnalyzerPro_Final.entity.Analysis;
import com.example.ResumeAnalyzerPro_Final.entity.Resume;
import com.example.ResumeAnalyzerPro_Final.entity.User;
import com.example.ResumeAnalyzerPro_Final.repository.AnalysisRepository;
import com.example.ResumeAnalyzerPro_Final.repository.ResumeRepository;
import com.example.ResumeAnalyzerPro_Final.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private static final String ADMIN_EMAIL = "admin@resumeanalyzer.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final int DEFAULT_ANALYSIS_PAGE_SIZE = 5;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private AnalysisRepository analysisRepository;

    public boolean isAdminLoginValid(String email, String password) {
        if (email == null || password == null) {
            return false;
        }
        return ADMIN_EMAIL.equalsIgnoreCase(email.trim()) && ADMIN_PASSWORD.equals(password);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByIdDesc();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<Resume> getUserResumes(User user) {
        return resumeRepository.findByUserOrderByIdDesc(user);
    }

    public Page<Analysis> getUserAnalyses(User user, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size > 0 ? size : DEFAULT_ANALYSIS_PAGE_SIZE;
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return analysisRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    public long getUserResumeCount(User user) {
        return resumeRepository.countByUser(user);
    }

    public long getUserAnalysisCount(User user) {
        return analysisRepository.countByUser(user);
    }

    @Transactional
    public boolean suspendUser(Long userId) {
        User user = getUserById(userId);
        if (user == null) {
            return false;
        }
        user.setActive(false);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean activateUser(Long userId) {
        User user = getUserById(userId);
        if (user == null) {
            return false;
        }
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean deleteUserAndRelatedData(Long userId) {
        User user = getUserById(userId);
        if (user == null) {
            return false;
        }

        analysisRepository.deleteByUser(user);
        resumeRepository.deleteByUser(user);
        userRepository.delete(user);
        return true;
    }
}
