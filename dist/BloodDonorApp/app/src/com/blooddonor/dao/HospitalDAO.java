package com.blooddonor.dao;

import com.blooddonor.db.DBConnection;
import com.blooddonor.model.Hospital;

import java.sql.*;

public class HospitalDAO {

    public boolean register(Hospital hospital) throws SQLException {
        String sql = "INSERT INTO hospitals (hospital_name, email, password, phone, address, city, license_no) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, hospital.getHospitalName());
            ps.setString(2, hospital.getEmail());
            ps.setString(3, hospital.getPassword());
            ps.setString(4, hospital.getPhone());
            ps.setString(5, hospital.getAddress());
            ps.setString(6, hospital.getCity());
            ps.setString(7, hospital.getLicenseNo());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        hospital.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public Hospital login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM hospitals WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHospital(rs);
                }
            }
        }
        return null;
    }

    public Hospital getHospitalById(int hospitalId) throws SQLException {
        String sql = "SELECT * FROM hospitals WHERE hospital_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hospitalId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHospital(rs);
                }
            }
        }
        return null;
    }

    private Hospital mapResultSetToHospital(ResultSet rs) throws SQLException {
        return new Hospital(
            rs.getInt("hospital_id"),
            rs.getString("hospital_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            rs.getString("address"),
            rs.getString("city"),
            rs.getString("license_no")
        );
    }
}
