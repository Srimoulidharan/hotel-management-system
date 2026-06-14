package com.hotel.dao;

import com.hotel.config.Database;
import com.hotel.model.Guest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {
    public List<Guest> findAll() throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests ORDER BY id DESC";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                guests.add(mapGuest(rs));
            }
        }
        return guests;
    }

    public Guest findById(int id) throws SQLException {
        String sql = "SELECT * FROM guests WHERE id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapGuest(rs);
                }
            }
        }
        return null;
    }

    public void create(Guest guest) throws SQLException {
        String sql = "INSERT INTO guests (full_name, email, phone, id_proof, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            fillGuestStatement(ps, guest);
            ps.executeUpdate();
        }
    }

    public void update(Guest guest) throws SQLException {
        String sql = "UPDATE guests SET full_name = ?, email = ?, phone = ?, id_proof = ?, address = ? WHERE id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            fillGuestStatement(ps, guest);
            ps.setInt(6, guest.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM guests WHERE id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM guests";
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1);
        }
    }

    private void fillGuestStatement(PreparedStatement ps, Guest guest) throws SQLException {
        ps.setString(1, guest.getFullName());
        ps.setString(2, guest.getEmail());
        ps.setString(3, guest.getPhone());
        ps.setString(4, guest.getIdProof());
        ps.setString(5, guest.getAddress());
    }

    private Guest mapGuest(ResultSet rs) throws SQLException {
        return new Guest(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("id_proof"),
                rs.getString("address")
        );
    }
}
