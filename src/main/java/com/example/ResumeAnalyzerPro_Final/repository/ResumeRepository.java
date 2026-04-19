package com.example.ResumeAnalyzerPro_Final.repository;

import com.example.ResumeAnalyzerPro_Final.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ResumeAnalyzerPro_Final.entity.Resume;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long>{

    List<Resume> findByUserOrderByIdDesc(User user);

    long countByUser(User user);

    void deleteByUser(User user);

}
