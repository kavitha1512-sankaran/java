package com.blooddonor.dao;

import com.blooddonor.db.DBConnection;
import com.blooddonor.model.BloodRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BloodRequestDAO {

    public boolean createRequest(BloodRequest req) throws SQLException {
        String sql = "INSERT INTO blood_requests (hospital_id, blood_group, units_needed, urgency_level, patient_case, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, req.getHospitalId());
            ps.setString(2, req.getBloodGroup());
            ps.setInt(3, req.getUnitsNeeded());
            ps.setString(4, req.getUrgencyLevel());
            ps.setString(5, req.getPatientCase());
            ps.setString(6, req.getStatus() != null ? req.getStatus() : "Pending");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        req.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public List<BloodRequest> getAllActiveRequests() throws SQLException {
        String sql = "SELECT br.*, h.hospital_name, h.phone AS hospital_phone, h.city AS hospital_city " +
                     "FROM blood_requests br " +
                     "JOIN hospitals h ON br.hospital_id = h.hospital_id " +
                     "WHERE br.status = 'Pending' " +
                     "ORDER BY FIELD(br.urgency_level, 'Critical', 'Urgent', 'Normal'), br.request_date DESC";
        List<BloodRequest> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToRequest(rs));
            }
        }
        return list;
    }

    public List<BloodRequest> getRequestsByHospital(int hospitalId) throws SQLException {
        String sql = "SELECT br.*, h.hospital_name, h.phone AS hospital_phone, h.city AS hospital_city " +
                     "FROM blood_requests br " +
                     "JOIN hospitals h ON br.hospital_id = h.hospital_id " +
                     "WHERE br.hospital_id = ? " +
                     "ORDER BY br.request_date DESC";
        List<BloodRequest> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hospitalId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRequest(rs));
                }
            }
        }
        return list;
    }

    public boolean updateStatus(int requestId, String status) throws SQLException {
        String sql = "UPDATE blood_requests SET status = ? WHERE request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        }
    }

    private BloodRequest mapResultSetToRequest(ResultSet rs) throws SQLException {
        return new BloodRequest(
            rs.getInt("request_id"),
            rs.getInt("hospital_id"),
            rs.getString("hospital_name"),
            rs.getString("hospital_phone"),
            rs.getString("hospital_city"),
            rs.getString("blood_group"),
            rs.getInt("units_needed"),
            rs.getString("urgency_level"),
            rs.getString("patient_case"),
            rs.getString("status"),
            rs.getTimestamp("request_date")
        );
    }
}
