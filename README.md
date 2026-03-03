# Campus Event Registration & Management System

A comprehensive Java Swing application designed to facilitate campus event management and student participation. This system provides a streamlined interface for administrators to organize events and for students to discover and register for them.

## 🚀 Features

- **Multi-Role Authentication**: Secure login system with distinct dashboards for **Administrators** and **Students**.
- **Admin Dashboard**:
  - Create and manage campus events (Title, Description, Date, Location, Capacity).
  - Monitor event registrations.
- **Student Dashboard**:
  - Browse available events.
  - One-click registration for upcoming events.
- **Database Integration**: Robust data persistence using MySQL.
- **Modern UI**: Clean and intuitive Graphical User Interface built with Java Swing.

## 🛠️ Tech Stack

- **Language**: Java
- **Database**: MySQL
- **GUI Framework**: Swing
- **Driver**: MySQL Connector/J

## 📋 Prerequisites

- Java Development Kit (JDK) 8 or higher.
- MySQL Server installed and running.

## ⚙️ Setup Instructions

1. **Database Configuration**:
   - Run the provided `db_setup.sql` script in your MySQL environment to initialize the database and tables.
2. **JAR Dependencies**:
   - Ensure `mysql-connector-j-9.6.0.jar` is added to your project's build path/classpath.
3. **Running the Application**:
   - Run the `com.campus.Main` class to start the application.

## 📂 Project Structure

```text
src/com/campus/
├── db/         # Database connection and initialization logic
├── models/     # Data entities (User, Event, Registration)
├── ui/         # Swing UI components (Login, Dashboards)
└── Main.java   # App entry point
```

## 🤝 Contributing

Feel free to fork this project and submit pull requests for any features or bug fixes.
