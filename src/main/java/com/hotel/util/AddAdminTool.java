package com.hotel.util;

import com.hotel.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AddAdminTool {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("===== ADMIN USER MANAGEMENT =====");
            System.out.println("1. View Admin Users");
            System.out.println("2. Add / Update Admin User");
            System.out.println("3. Delete Admin User");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAdmins();
                    break;

                case "2":
                    addOrUpdateAdmin(scanner);
                    break;

                case "3":
                    deleteAdmin(scanner);
                    break;

                case "4":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAdmins() {
        String sql = "SELECT id, username, full_name FROM admin_users ORDER BY id";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println();
            System.out.println("===== ADMIN USERS =====");
            System.out.printf("%-5s %-20s %-30s%n", "ID", "USERNAME", "FULL NAME");
            System.out.println("------------------------------------------------------------");

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-20s %-30s%n",
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("full_name"));
            }

            if (!found) {
                System.out.println("No admin users found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addOrUpdateAdmin(Scanner scanner) {
        System.out.println();
        System.out.println("===== ADD / UPDATE ADMIN =====");

        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();

        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        System.out.print("Enter admin full name: ");
        String fullName = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            System.out.println("Username, password and full name cannot be empty.");
            return;
        }

        String sql = "MERGE INTO admin_users (username, password_hash, full_name) " +
                     "KEY(username) VALUES (?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, HashUtil.sha256(password));
            ps.setString(3, fullName);

            ps.executeUpdate();

            System.out.println("Admin added/updated successfully.");
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteAdmin(Scanner scanner) {
        System.out.println();
        System.out.println("===== DELETE ADMIN =====");

        viewAdmins();

        System.out.print("Enter admin ID to delete: ");
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            System.out.println("Admin ID cannot be empty.");
            return;
        }

        int id;

        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
            return;
        }

        if (id == 1) {
            System.out.println("Default admin cannot be deleted for safety.");
            return;
        }

        System.out.print("Are you sure you want to delete admin ID '" + id + "'? Type YES: ");
        String confirm = scanner.nextLine();

        if (!confirm.equals("YES")) {
            System.out.println("Delete cancelled.");
            return;
        }

        String sql = "DELETE FROM admin_users WHERE id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Admin deleted successfully.");
            } else {
                System.out.println("Admin not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}