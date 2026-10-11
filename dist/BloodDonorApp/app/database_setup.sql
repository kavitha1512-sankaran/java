-- =======================================================
-- BLOOD DONOR AVAILABILITY SYSTEM DATABASE SETUP
-- =======================================================

CREATE DATABASE IF NOT EXISTS blood_donation_db;
USE blood_donation_db;

-- 1. DONORS TABLE
CREATE TABLE IF NOT EXISTS donors (
    donor_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    blood_group VARCHAR(5) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(15) NOT NULL,
    weight_kg DECIMAL(5,2) NOT NULL,
    city VARCHAR(80) NOT NULL,
    last_donation_date DATE NULL,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_donor_age CHECK (age >= 18)
);

-- 2. HOSPITALS TABLE
CREATE TABLE IF NOT EXISTS hospitals (
    hospital_id INT AUTO_INCREMENT PRIMARY KEY,
    hospital_name VARCHAR(150) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address TEXT NOT NULL,
    city VARCHAR(80) NOT NULL,
    license_no VARCHAR(60) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. BLOOD REQUESTS TABLE
CREATE TABLE IF NOT EXISTS blood_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    hospital_id INT NOT NULL,
    blood_group VARCHAR(5) NOT NULL,
    units_needed INT NOT NULL,
    urgency_level ENUM('Normal', 'Urgent', 'Critical') DEFAULT 'Normal',
    patient_case VARCHAR(150),
    status ENUM('Pending', 'Fulfilled', 'Cancelled') DEFAULT 'Pending',
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES hospitals(hospital_id) ON DELETE CASCADE
);

-- 4. DONATION HISTORY TABLE
CREATE TABLE IF NOT EXISTS donation_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    donor_id INT NOT NULL,
    hospital_id INT NOT NULL,
    donation_date DATE NOT NULL,
    units INT DEFAULT 1,
    remarks VARCHAR(255),
    FOREIGN KEY (donor_id) REFERENCES donors(donor_id) ON DELETE CASCADE,
    FOREIGN KEY (hospital_id) REFERENCES hospitals(hospital_id) ON DELETE CASCADE
);

-- =======================================================
-- SAMPLE DEMO DATA (FOR TESTING & DEMO)
-- =======================================================

-- Sample Donors (Passwords: donor123)
INSERT INTO donors (full_name, email, password, phone, blood_group, age, gender, weight_kg, city, last_donation_date, is_available)
VALUES 
('Jothika S', 'jothika@gmail.com', 'donor123', '9876543210', 'O+', 22, 'Female', 54.0, 'Chennai', '2026-05-10', TRUE),
('Kumar Vel', 'kumar@gmail.com', 'donor123', '9845123456', 'A+', 26, 'Male', 65.0, 'Chennai', '2026-04-15', TRUE),
('Priya Dharshini', 'priya@gmail.com', 'donor123', '9123456780', 'B+', 24, 'Female', 52.5, 'Coimbatore', '2026-09-01', FALSE),
('Arun Prakash', 'arun@gmail.com', 'donor123', '9789012345', 'O-', 29, 'Male', 70.0, 'Madurai', '2026-01-20', TRUE),
('Siva Sankaran', 'siva@gmail.com', 'donor123', '9988776655', 'AB+', 21, 'Male', 58.0, 'Trichy', NULL, TRUE),
('Divya R', 'divya@gmail.com', 'donor123', '9871234567', 'A-', 23, 'Female', 51.0, 'Chennai', '2026-06-12', TRUE)
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);

-- Sample Hospitals (Passwords: hosp123)
INSERT INTO hospitals (hospital_name, email, password, phone, address, city, license_no)
VALUES
('Apollo Speciality Hospital', 'apollo@hospital.com', 'hosp123', '044-28290200', '21 Greams Lane, Thousand Lights', 'Chennai', 'LIC-TN-CHE-1042'),
('KMCH Multi-Speciality Hospital', 'kmch@hospital.com', 'hosp123', '0422-4323800', 'Avinashi Road, Civil Aerodrome Post', 'Coimbatore', 'LIC-TN-CBE-2098'),
('Meenakshi Mission Hospital', 'mmhrc@hospital.com', 'hosp123', '0452-2588741', 'Lake Area, Melur Road', 'Madurai', 'LIC-TN-MDU-3105')
ON DUPLICATE KEY UPDATE hospital_name=VALUES(hospital_name);

-- Sample Blood Requests
INSERT INTO blood_requests (hospital_id, blood_group, units_needed, urgency_level, patient_case, status)
VALUES
(1, 'O+', 3, 'Critical', 'Emergency Cardiac Surgery', 'Pending'),
(1, 'A+', 2, 'Urgent', 'Accident Trauma Patient', 'Pending'),
(2, 'O-', 1, 'Critical', 'Rare Blood Group ICU Requirement', 'Pending'),
(3, 'B+', 4, 'Normal', 'Scheduled Orthopedic Surgery', 'Pending')
ON DUPLICATE KEY UPDATE units_needed=VALUES(units_needed);
