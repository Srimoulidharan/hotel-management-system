package com.hotel.config;

import com.hotel.util.HashUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
	private static final String DB_PATH = System.getenv().getOrDefault("H2_DB_PATH", "~/hotel_management_system");
	private static final String URL = "jdbc:h2:" + DB_PATH + ";AUTO_SERVER=TRUE;MODE=MySQL;DATABASE_TO_UPPER=false";
	private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static volatile boolean initialized = false;

    private Database() {
    }

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        initialize();
        return rawConnection();
    }

    private static Connection rawConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static synchronized void initialize() throws SQLException {
        if (initialized) {
            return;
        }

        try (Connection connection = rawConnection(); Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS admin_users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) NOT NULL UNIQUE, "
                    + "password_hash VARCHAR(64) NOT NULL, "
                    + "full_name VARCHAR(100) NOT NULL"
                    + ")");

            statement.execute("CREATE TABLE IF NOT EXISTS rooms ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "room_number VARCHAR(20) NOT NULL UNIQUE, "
                    + "room_type VARCHAR(50) NOT NULL, "
                    + "price_per_night DECIMAL(10,2) NOT NULL, "
                    + "status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE', "
                    + "description VARCHAR(255)"
                    + ")");

            statement.execute("CREATE TABLE IF NOT EXISTS guests ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "full_name VARCHAR(120) NOT NULL, "
                    + "email VARCHAR(120), "
                    + "phone VARCHAR(30) NOT NULL, "
                    + "id_proof VARCHAR(80), "
                    + "address VARCHAR(255)"
                    + ")");

            statement.execute("CREATE TABLE IF NOT EXISTS bookings ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "guest_id INT NOT NULL, "
                    + "room_id INT NOT NULL, "
                    + "check_in DATE NOT NULL, "
                    + "check_out DATE NOT NULL, "
                    + "status VARCHAR(20) NOT NULL DEFAULT 'RESERVED', "
                    + "total_amount DECIMAL(10,2) NOT NULL, "
                    + "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (guest_id) REFERENCES guests(id), "
                    + "FOREIGN KEY (room_id) REFERENCES rooms(id)"
                    + ")");

            seedAdmin(connection);
            seedRooms(connection);
        }

        initialized = true;
    }

    private static void seedAdmin(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM admin_users")) {
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                return;
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO admin_users (username, password_hash, full_name) VALUES (?, ?, ?)")) {
            ps.setString(1, "admin");
            ps.setString(2, HashUtil.sha256("admin123"));
            ps.setString(3, "Hotel Administrator");
            ps.executeUpdate();
        }
    }

    private static void seedRooms(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM rooms")) {
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                return;
            }
        }

        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, status, description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            insertRoom(ps, "101", "Single", new BigDecimal("1200.00"), "AVAILABLE", "Comfortable single room");
            insertRoom(ps, "102", "Single", new BigDecimal("1200.00"), "AVAILABLE", "Single room with city view");
            insertRoom(ps, "201", "Double", new BigDecimal("2200.00"), "AVAILABLE", "Double room for two guests");
            insertRoom(ps, "202", "Double", new BigDecimal("2400.00"), "AVAILABLE", "Double room with balcony");
            insertRoom(ps, "301", "Deluxe", new BigDecimal("3500.00"), "AVAILABLE", "Deluxe room with premium amenities");
            insertRoom(ps, "401", "Suite", new BigDecimal("5500.00"), "AVAILABLE", "Luxury suite room");
        }
    }

    private static void insertRoom(PreparedStatement ps, String number, String type, BigDecimal price, String status,
                                   String description) throws SQLException {
        ps.setString(1, number);
        ps.setString(2, type);
        ps.setBigDecimal(3, price);
        ps.setString(4, status);
        ps.setString(5, description);
        ps.executeUpdate();
    }
}
