-- =====================================================
-- DATABASE: RevHire (UPDATED AS PER REQUIREMENTS)
-- =====================================================
CREATE DATABASE IF NOT EXISTS revhire;
USE revhire;

-- =====================================================
-- USERS TABLE (Authentication Base)
-- =====================================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('JOB_SEEKER', 'EMPLOYER') NOT NULL,
    security_question VARCHAR(255),
    security_answer VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- JOB SEEKER TABLE
-- (profile_completion REMOVED)
-- =====================================================
CREATE TABLE job_seeker (
    seeker_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    full_name VARCHAR(100),
    phone VARCHAR(15),
    location VARCHAR(100),
    experience_years INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- =====================================================
-- EMPLOYER TABLE
-- (Company + Employer MERGED)
-- (website, size REMOVED)
-- =====================================================
CREATE TABLE employer (
    employer_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    company_name VARCHAR(150) NOT NULL,
    industry VARCHAR(100),
    description TEXT,
    location VARCHAR(100),
    designation VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- =====================================================
-- RESUME TABLE
-- (projects REMOVED)
-- =====================================================
CREATE TABLE resume (
    resume_id INT AUTO_INCREMENT PRIMARY KEY,
    seeker_id INT NOT NULL UNIQUE,
    objective TEXT,
    education TEXT,
    experience TEXT,
    skills TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (seeker_id) REFERENCES job_seeker(seeker_id)
        ON DELETE CASCADE
);

-- =====================================================
-- JOB POST TABLE
-- (salary_min & salary_max MERGED INTO salary)
-- =====================================================
CREATE TABLE job_post (
    job_id INT AUTO_INCREMENT PRIMARY KEY,
    employer_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    skills TEXT,
    experience_required INT,
    education VARCHAR(150),
    location VARCHAR(100),
    salary DECIMAL(10,2),
    job_type ENUM('FULL_TIME', 'PART_TIME', 'INTERNSHIP', 'CONTRACT'),
    deadline DATE,
    status ENUM('OPEN', 'CLOSED') DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employer_id) REFERENCES employer(employer_id)
        ON DELETE CASCADE
);

-- =====================================================
-- JOB APPLICATION TABLE
-- (cover_letter REMOVED)
-- (application_comment MERGED HERE)
-- =====================================================
CREATE TABLE job_application (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    job_id INT NOT NULL,
    seeker_id INT NOT NULL,
    resume_id INT NOT NULL,
    status ENUM('APPLIED', 'SHORTLISTED', 'REJECTED', 'WITHDRAWN') DEFAULT 'APPLIED',
    applied_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    withdrawn_reason VARCHAR(255),
    employer_comment TEXT,
    FOREIGN KEY (job_id) REFERENCES job_post(job_id)
        ON DELETE CASCADE,
    FOREIGN KEY (seeker_id) REFERENCES job_seeker(seeker_id)
        ON DELETE CASCADE,
    FOREIGN KEY (resume_id) REFERENCES resume(resume_id)
        ON DELETE CASCADE,
    UNIQUE (job_id, seeker_id)
);

-- =====================================================
-- NOTIFICATION TABLE
-- =====================================================
CREATE TABLE notification (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    message VARCHAR(255) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);
--select * from revhire.users;
--SELECT * FROM job_post WHERE employer_id = 3;
--SELECT a.application_id, a.status
--FROM job_application a
--JOIN job_post j ON a.job_id = j.job_id
--WHERE j.employer_id = 3;
