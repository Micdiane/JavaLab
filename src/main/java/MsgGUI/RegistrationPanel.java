package MsgGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationPanel extends JPanel {
    private JTextField emailTextField;
    private JPasswordField passwordField;

    public RegistrationPanel() {
        setLayout(new GridBagLayout());

        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());

                if (registerUser(email, password)) {
                    JOptionPane.showMessageDialog(RegistrationPanel.this, "User registered successfully");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(RegistrationPanel.this, "Failed to register user");
                }
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;

        add(emailLabel, constraints);

        constraints.gridx = 1;
        add(emailTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(passwordLabel, constraints);

        constraints.gridx = 1;
        add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(registerButton, constraints);
    }

    private boolean registerUser(String email, String password) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/javaFinal";
        String username = "root";
        String dbPassword = "root";

        // 检查邮箱是否已被注册
        if (isEmailRegistered(email)) {
            return false;
        }

        // 插入用户数据的SQL语句
        String insertQuery = "INSERT INTO users (email, password) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, dbPassword);
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {

            statement.setString(1, email);
            statement.setString(2, password);

            // 执行插入操作
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean isEmailRegistered(String email) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/javaFinal";
        String username = "root";
        String dbPassword = "root";

        // 查询准备语句
        String query = "SELECT * FROM users WHERE email=?";

        try (Connection connection = DriverManager.getConnection(url, username, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);

            // 执行查询
            try (ResultSet resultSet = statement.executeQuery()) {
                // 如果结果集中有行，则表示邮箱已被注册
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void clearFields() {
        emailTextField.setText("");
        passwordField.setText("");
    }

}