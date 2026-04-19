Resume Analyzer System

Project Overview

The Resume Analyzer System is a web-based application that analyzes resumes based on a given job description. It calculates a match score, identifies matched and missing skills, and recommends suitable job roles.

The system helps users improve their resumes by providing clear insights and suggestions.

---

Features

- Resume upload (PDF/Text)
- Job description based analysis
- Match score calculation
- Matched skills identification
- Missing skills suggestion
- Recommended job roles
- Dashboard with analysis history
- Admin panel for user management

---

Technologies Used

- Backend: Spring Boot (Java)
- Frontend: HTML, CSS, JavaScript (Thymeleaf)
- Database: MySQL
- Libraries: Apache Tika (for resume parsing)

---

How It Works

1. User uploads resume
2. Apache Tika extracts text from resume
3. System compares resume with job description
4. Keywords and skills are matched
5. Score is calculated
6. Results are displayed on dashboard

---

Modules

- User Module
- Resume Upload Module
- Analysis Module
- Dashboard Module
- Admin Module

---

🗄️ Database

Database includes tables such as:

- users
- resume
- analyses
- analysis_matched_skills
- analysis_missing_skills
- analysis_recommended_jobs

---

How to Run the Project

1. Open project in IntelliJ
2. Configure MySQL database
3. Update "application.properties"
4. Run Spring Boot application
5. Open browser:
   http://localhost:9004

---

Conclusion

This project demonstrates how resume analysis can be automated using keyword matching and text processing techniques to assist users in improving their job readiness.
