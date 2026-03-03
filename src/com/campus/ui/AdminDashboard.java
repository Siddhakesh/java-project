package com.campus.ui;

import com.campus.db.DBConnection;
import com.campus.models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {
    private User currentUser;
    private JTable eventTable;
    private DefaultTableModel tableModel;

    public AdminDashboard(User user) {
        this.currentUser = user;
        setTitle("Admin Dashboard - " + user.getUsername());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.add(new JLabel("Admin: " + user.getUsername()));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        header.add(logoutBtn);
        add(header, BorderLayout.NORTH);

        // Sidebar / Actions
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addBtn = new JButton("Add Event");
        JButton refreshBtn = new JButton("Refresh");
        JButton deleteBtn = new JButton("Delete Event");

        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidePanel.add(addBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(refreshBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(deleteBtn);
        add(sidePanel, BorderLayout.WEST);

        // Event Table
        String[] columns = {"ID", "Title", "Date", "Location", "Capacity"};
        tableModel = new DefaultTableModel(columns, 0);
        eventTable = new JTable(tableModel);
        loadEvents();
        add(new JScrollPane(eventTable), BorderLayout.CENTER);

        // Button Actions
        addBtn.addActionListener(e -> showAddEventDialog());
        refreshBtn.addActionListener(e -> loadEvents());
        deleteBtn.addActionListener(e -> deleteSelectedEvent());
    }

    private void loadEvents() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM events";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getTimestamp("event_date"),
                    rs.getString("location"),
                    rs.getInt("capacity")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddEventDialog() {
        JTextField title = new JTextField();
        JTextField date = new JTextField("2026-04-15 10:00:00");
        JTextField loc = new JTextField();
        JTextField cap = new JTextField("100");
        Object[] msg = {"Title:", title, "Date (YYYY-MM-DD HH:MM:SS):", date, "Location:", loc, "Capacity:", cap};
        int option = JOptionPane.showConfirmDialog(this, msg, "Add New Event", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO events (title, event_date, location, capacity) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, title.getText());
                pstmt.setString(2, date.getText());
                pstmt.setString(3, loc.getText());
                pstmt.setInt(4, Integer.parseInt(cap.getText()));
                pstmt.executeUpdate();
                loadEvents();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding event: " + e.getMessage());
            }
        }
    }

    private void deleteSelectedEvent() {
        int row = eventTable.getSelectedRow();
        if (row == -1) return;
        int id = (int) tableModel.getValueAt(row, 0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM events WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            loadEvents();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
