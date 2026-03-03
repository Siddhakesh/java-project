package com.campus.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBInitializer {
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Update if needed

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
                Statement stmt = conn.createStatement()) {

            // Create database
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS campus_events_db");
            stmt.executeUpdate("USE campus_events_db");

            // Create users table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL UNIQUE," +
                    "password VARCHAR(255) NOT NULL," +
                    "role ENUM('student', 'admin') DEFAULT 'student'," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Create events table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS events (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(100) NOT NULL," +
                    "description TEXT," +
                    "event_date DATETIME NOT NULL," +
                    "location VARCHAR(100)," +
                    "capacity INT DEFAULT 100," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Create registrations table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS registrations (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "event_id INT NOT NULL," +
                    "registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE," +
                    "UNIQUE(user_id, event_id))");

            // Seed admin if not exists
            stmt.executeUpdate(
                    "INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin')");
            stmt.executeUpdate(
                    "INSERT IGNORE INTO users (username, password, role) VALUES ('student', 'student123', 'student')");

            System.out.println("Database initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
