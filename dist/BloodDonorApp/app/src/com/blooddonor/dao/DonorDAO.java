package com.blooddonor.dao;

import com.blooddonor.db.DBConnection;
import com.blooddonor.model.Donor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonorDAO {

    public boolean register(Donor donor) throws SQLException {
        String sql = "INSERT INTO donors (full_name, email, password, phone, blood_group, age, gender, weight_kg, city, last_donation_date, is_available) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, donor.getFullName());
            ps.setString(2, donor.getEmail());
            ps.setString(3, donor.getPassword());
            ps.setString(4, donor.getPhone());
            ps.setString(5, donor.getBloodGroup());
            ps.setInt(6, donor.getAge());
            ps.setString(7, donor.getGender());
            ps.setDouble(8, donor.getWeightKg());
            ps.setString(9, donor.getCity());
            ps.setDate(10, donor.getLastDonationDate());
            ps.setBoolean(11, donor.isAvailable());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        donor.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public Donor login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM donors WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDonor(rs);
                }
            }
        }
        return null;
    }

    public boolean updateProfile(Donor donor) throws SQLException {
        String sql = "UPDATE donors SET full_name = ?, phone = ?, blood_group = ?, age = ?, " +
                     "gender = ?, weight_kg = ?, city = ?, last_donation_date = ?, is_available = ? WHERE donor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, donor.getFullName());
            ps.setString(2, donor.getPhone());
            ps.setString(3, donor.getBloodGroup());
            ps.setInt(4, donor.getAge());
            ps.setString(5, donor.getGender());
            ps.setDouble(6, donor.getWeightKg());
            ps.setString(7, donor.getCity());
            ps.setDate(8, donor.getLastDonationDate());
            ps.setBoolean(9, donor.isAvailable());
            ps.setInt(10, donor.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean toggleAvailability(int donorId, boolean isAvailable) throws SQLException {
        String sql = "UPDATE donors SET is_available = ? WHERE donor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isAvailable);
            ps.setInt(2, donorId);
            return ps.executeUpdate() > 0;
        }
    }

    public Donor getDonorById(int donorId) throws SQLException {
        String sql = "SELECT * FROM donors WHERE donor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, donorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDonor(rs);
                }
            }
        }
        return null;
    }

    public List<Donor> searchDonors(String bloodGroup, String city, boolean onlyAvailable) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM donors WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (bloodGroup != null && !bloodGroup.trim().isEmpty() && !bloodGroup.equalsIgnoreCase("ALL")) {
            sql.append("AND blood_group = ? ");
            params.add(bloodGroup.trim());
        }

        if (city != null && !city.trim().isEmpty()) {
            sql.append("AND LOWER(city) LIKE ? ");
            params.add("%" + city.trim().toLowerCase() + "%");
        }

        if (onlyAvailable) {
            sql.append("AND is_available = TRUE ");
        }

        sql.append("ORDER BY is_available DESC, city ASC");

        List<Donor> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDonor(rs));
                }
            }
        }
        return list;
    }

    private Donor mapResultSetToDonor(ResultSet rs) throws SQLException {
        return new Donor(
            rs.getInt("donor_id"),
            rs.getString("full_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            rs.getString("blood_group"),
            rs.getInt("age"),
            rs.getString("gender"),
            rs.getDouble("weight_kg"),
            rs.getString("city"),
            rs.getDate("last_donation_date"),
            rs.getBoolean("is_available")
        );
    }
}
