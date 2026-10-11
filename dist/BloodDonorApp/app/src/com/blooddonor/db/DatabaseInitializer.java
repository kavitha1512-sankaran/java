package com.blooddonor.db;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static boolean initialize() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Donors Table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS donors (" +
                "  donor_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  full_name VARCHAR(100) NOT NULL," +
                "  email VARCHAR(100) UNIQUE NOT NULL," +
                "  password VARCHAR(100) NOT NULL," +
                "  phone VARCHAR(15) NOT NULL," +
                "  blood_group VARCHAR(5) NOT NULL," +
                "  age INT NOT NULL," +
                "  gender VARCHAR(15) NOT NULL," +
                "  weight_kg DECIMAL(5,2) NOT NULL," +
                "  city VARCHAR(80) NOT NULL," +
                "  last_donation_date DATE NULL," +
                "  is_available BOOLEAN DEFAULT TRUE," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            // 2. Hospitals Table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS hospitals (" +
                "  hospital_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  hospital_name VARCHAR(150) NOT NULL," +
                "  email VARCHAR(100) UNIQUE NOT NULL," +
                "  password VARCHAR(100) NOT NULL," +
                "  phone VARCHAR(15) NOT NULL," +
                "  address TEXT NOT NULL," +
                "  city VARCHAR(80) NOT NULL," +
                "  license_no VARCHAR(60) NOT NULL," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            // 3. Blood Requests Table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS blood_requests (" +
                "  request_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  hospital_id INT NOT NULL," +
                "  blood_group VARCHAR(5) NOT NULL," +
                "  units_needed INT NOT NULL," +
                "  urgency_level VARCHAR(20) DEFAULT 'Normal'," +
                "  patient_case VARCHAR(150)," +
                "  status VARCHAR(20) DEFAULT 'Pending'," +
                "  request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (hospital_id) REFERENCES hospitals(hospital_id) ON DELETE CASCADE" +
                ")"
            );

            // 4. Donation History Table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS donation_history (" +
                "  history_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  donor_id INT NOT NULL," +
                "  hospital_id INT NOT NULL," +
                "  donation_date DATE NOT NULL," +
                "  units INT DEFAULT 1," +
                "  remarks VARCHAR(255)," +
                "  FOREIGN KEY (donor_id) REFERENCES donors(donor_id) ON DELETE CASCADE," +
                "  FOREIGN KEY (hospital_id) REFERENCES hospitals(hospital_id) ON DELETE CASCADE" +
                ")"
            );

            // Insert sample demo records if donors table is empty
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM donors");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.executeUpdate(
                    "INSERT INTO donors (full_name, email, password, phone, blood_group, age, gender, weight_kg, city, last_donation_date, is_available) VALUES " +
                    "('Jothika S', 'jothika@gmail.com', 'donor123', '9876543210', 'O+', 22, 'Female', 54.0, 'Chennai', '2026-05-10', TRUE)," +
                    "('Kumar Vel', 'kumar@gmail.com', 'donor123', '9845123456', 'A+', 26, 'Male', 65.0, 'Chennai', '2026-04-15', TRUE)," +
                    "('Priya Dharshini', 'priya@gmail.com', 'donor123', '9123456780', 'B+', 24, 'Female', 52.5, 'Coimbatore', '2026-09-01', FALSE)," +
                    "('Arun Prakash', 'arun@gmail.com', 'donor123', '9789012345', 'O-', 29, 'Male', 70.0, 'Madurai', '2026-01-20', TRUE)," +
                    "('Divya R', 'divya@gmail.com', 'donor123', '9871234567', 'A-', 23, 'Female', 51.0, 'Chennai', '2026-06-12', TRUE)"
                );
            }

            var rsHosp = stmt.executeQuery("SELECT COUNT(*) FROM hospitals");
            if (rsHosp.next() && rsHosp.getInt(1) == 0) {
                stmt.executeUpdate(
                    "INSERT INTO hospitals (hospital_name, email, password, phone, address, city, license_no) VALUES " +
                    "('Apollo Speciality Hospital', 'apollo@hospital.com', 'hosp123', '044-28290200', '21 Greams Lane, Thousand Lights', 'Chennai', 'LIC-TN-CHE-1042')," +
                    "('KMCH Multi-Speciality Hospital', 'kmch@hospital.com', 'hosp123', '0422-4323800', 'Avinashi Road', 'Coimbatore', 'LIC-TN-CBE-2098')"
                );

                stmt.executeUpdate(
                    "INSERT INTO blood_requests (hospital_id, blood_group, units_needed, urgency_level, patient_case, status) VALUES " +
                    "(1, 'O+', 3, 'Critical', 'Emergency Cardiac Surgery', 'Pending')," +
                    "(1, 'A+', 2, 'Urgent', 'Accident Trauma Patient', 'Pending')," +
                    "(2, 'O-', 1, 'Critical', 'Rare Blood Group ICU Requirement', 'Pending')"
                );
            }

            return true;
        } catch (Exception e) {
            System.err.println("Database auto-init warning: " + e.getMessage());
            return false;
        }
    }
}
