package com.campus.ui;

import com.campus.db.DBConnection;
import com.campus.models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn, signupBtn;

    public LoginFrame() {
        setTitle("Campus Event Management - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Campus Event Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        userField = new JTextField(15);
        gbc.gridx = 1; add(userField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);
        passField = new JPasswordField(15);
        gbc.gridx = 1; add(passField, gbc);

        // Buttons
        JPanel btnPanel = new JPanel();
        loginBtn = new JButton("Login");
        signupBtn = new JButton("Sign Up");
        btnPanel.add(loginBtn);
        btnPanel.add(signupBtn);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        loginBtn.addActionListener(e -> handleLogin());
        signupBtn.addActionListener(e -> handleSignup());
    }

    private void handleLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"), 
                                     rs.getString("password"), rs.getString("role"));
                JOptionPane.showMessageDialog(this, "Welcome " + user.getUsername());
                
                if (user.getRole().equals("admin")) {
                    new AdminDashboard(user).setVisible(true);
                } else {
                    new StudentDashboard(user).setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void handleSignup() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter details");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'student')";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Signup Successful! You can now login.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Signup failed: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
