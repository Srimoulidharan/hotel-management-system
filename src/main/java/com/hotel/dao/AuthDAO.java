package com.hotel.dao;

import com.hotel.config.Database;
import com.hotel.model.AdminUser;
import com.hotel.util.HashUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDAO {
    public AdminUser authenticate(String username, String password) throws SQLException {
        String sql = "SELECT id, username, full_name FROM admin_users WHERE username = ? AND password_hash = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, HashUtil.sha256(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AdminUser(rs.getInt("id"), rs.getString("username"), rs.getString("full_name"));
                }
            }
        }
        return null;
    }
}
