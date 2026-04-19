package com.example.ResumeAnalyzerPro_Final.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ResumeAnalyzerPro_Final.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);

    List<User> findAllByOrderByIdDesc();

}
