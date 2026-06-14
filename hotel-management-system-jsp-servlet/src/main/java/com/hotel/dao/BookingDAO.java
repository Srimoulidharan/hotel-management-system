package com.hotel.dao;

import com.hotel.config.Database;
import com.hotel.model.Booking;
import com.hotel.model.BookingView;
import com.hotel.model.Room;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private final RoomDAO roomDAO = new RoomDAO();

    public List<BookingView> findAllWithDetails() throws SQLException {
        List<BookingView> bookings = new ArrayList<>();
        String sql = "SELECT b.*, g.full_name AS guest_name, g.phone AS guest_phone, "
                + "r.room_number, r.room_type "
                + "FROM bookings b "
                + "JOIN guests g ON b.guest_id = g.id "
                + "JOIN rooms r ON b.room_id = r.id "
                + "ORDER BY b.id DESC";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BookingView view = mapBookingView(rs);
                bookings.add(view);
            }
        }
        return bookings;
    }

    public void create(Booking booking) throws SQLException {
        Room room = roomDAO.findById(booking.getRoomId());
        if (room == null) {
            throw new SQLException("Selected room was not found.");
        }
        if ("MAINTENANCE".equalsIgnoreCase(room.getStatus())) {
            throw new SQLException("Selected room is under maintenance.");
        }
        if (isRoomBooked(booking.getRoomId(), booking.getCheckIn(), booking.getCheckOut())) {
            throw new SQLException("Selected room is already booked for the selected dates.");
        }

        long nights = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
        if (nights <= 0) {
            throw new SQLException("Check-out date must be after check-in date.");
        }
        BigDecimal total = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));
        booking.setTotalAmount(total);
        booking.setStatus("RESERVED");

        String sql = "INSERT INTO bookings (guest_id, room_id, check_in, check_out, status, total_amount) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, booking.getGuestId());
            ps.setInt(2, booking.getRoomId());
            ps.setDate(3, Date.valueOf(booking.getCheckIn()));
            ps.setDate(4, Date.valueOf(booking.getCheckOut()));
            ps.setString(5, booking.getStatus());
            ps.setBigDecimal(6, booking.getTotalAmount());
            ps.executeUpdate();
        }
    }

    public boolean isRoomBooked(int roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings "
                + "WHERE room_id = ? "
                + "AND status IN ('RESERVED', 'CHECKED_IN') "
                + "AND NOT (check_out <= ? OR check_in >= ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setDate(2, Date.valueOf(checkIn));
            ps.setDate(3, Date.valueOf(checkOut));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public void updateStatus(int bookingId, String status) throws SQLException {
        String roomLookup = "SELECT room_id FROM bookings WHERE id = ?";
        Integer roomId = null;
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(roomLookup)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                }
            }
        }

        if (roomId == null) {
            throw new SQLException("Booking not found.");
        }

        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        }

        if ("CHECKED_IN".equals(status)) {
            roomDAO.updateStatus(roomId, "OCCUPIED");
        } else if ("CHECKED_OUT".equals(status) || "CANCELLED".equals(status)) {
            roomDAO.updateStatus(roomId, "AVAILABLE");
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int countActive() throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE status IN ('RESERVED', 'CHECKED_IN')";
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public BigDecimal totalRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM bookings WHERE status = 'CHECKED_OUT'";
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            rs.next();
            return rs.getBigDecimal(1);
        }
    }

    private BookingView mapBookingView(ResultSet rs) throws SQLException {
        BookingView booking = new BookingView();
        booking.setId(rs.getInt("id"));
        booking.setGuestId(rs.getInt("guest_id"));
        booking.setRoomId(rs.getInt("room_id"));
        booking.setCheckIn(rs.getDate("check_in").toLocalDate());
        booking.setCheckOut(rs.getDate("check_out").toLocalDate());
        booking.setStatus(rs.getString("status"));
        booking.setTotalAmount(rs.getBigDecimal("total_amount"));
        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            booking.setCreatedAt(timestamp.toLocalDateTime());
        }
        booking.setGuestName(rs.getString("guest_name"));
        booking.setGuestPhone(rs.getString("guest_phone"));
        booking.setRoomNumber(rs.getString("room_number"));
        booking.setRoomType(rs.getString("room_type"));
        return booking;
    }
}
