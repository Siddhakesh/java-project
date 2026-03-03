package com.campus.ui;

import com.campus.db.DBConnection;
import com.campus.models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDashboard extends JFrame {
    private User currentUser;
    private JTable eventTable;
    private DefaultTableModel tableModel;

    public StudentDashboard(User user) {
        this.currentUser = user;
        setTitle("Student Dashboard - " + user.getUsername());
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.add(new JLabel("Welcome, " + user.getUsername() + " | Role: Student"));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        header.add(logoutBtn);
        add(header, BorderLayout.NORTH);

        // Event Table
        String[] columns = {"ID", "Title", "Description", "Date", "Location", "Capacity"};
        tableModel = new DefaultTableModel(columns, 0);
        eventTable = new JTable(tableModel);
        loadEvents();
        add(new JScrollPane(eventTable), BorderLayout.CENTER);

        // Actions
        JPanel actions = new JPanel();
        JButton registerBtn = new JButton("Register for Selected Event");
        registerBtn.addActionListener(e -> registerEvent());
        actions.add(registerBtn);
        add(actions, BorderLayout.SOUTH);
    }

    private void loadEvents() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM events";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getTimestamp("event_date"),
                    rs.getString("location"),
                    rs.getInt("capacity")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void registerEvent() {
        int row = eventTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an event first!");
            return;
        }

        int eventId = (int) tableModel.getValueAt(row, 0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO registrations (user_id, event_id) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentUser.getId());
            pstmt.setInt(2, eventId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Successfully registered for event!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Already registered or error: " + e.getMessage());
        }
    }
}
