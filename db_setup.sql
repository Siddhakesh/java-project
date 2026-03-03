-- Database initialization script for Campus Event Registration & Management System
CREATE DATABASE IF NOT EXISTS campus_events_db;
USE campus_events_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('student', 'admin') DEFAULT 'student',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Events table
CREATE TABLE IF NOT EXISTS events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    event_date DATETIME NOT NULL,
    location VARCHAR(100),
    capacity INT DEFAULT 100,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Registrations table
CREATE TABLE IF NOT EXISTS registrations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    UNIQUE(user_id, event_id)
);

-- Seed data for testing
INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');
INSERT IGNORE INTO users (username, password, role) VALUES ('student', 'student123', 'student');

INSERT IGNORE INTO events (title, description, event_date, location, capacity) VALUES 
('Annual Tech Fest', 'A day of coding and innovation', '2026-04-15 09:00:00', 'Main Auditorium', 200),
('Art & Culture Night', 'Celebrate local artists and performers', '2026-05-10 18:00:00', 'Student Center', 150);
