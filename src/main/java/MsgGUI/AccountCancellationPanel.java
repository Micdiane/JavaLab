package MsgGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AccountCancellationPanel extends JPanel {
    private JTextField emailTextField;
    private JPasswordField passwordField;

    public AccountCancellationPanel() {
        setLayout(new GridBagLayout());

        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        JButton cancelAccountButton = new JButton("Cancel Account");
        cancelAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());

                if (cancelAccount(email, password)) {
                    JOptionPane.showMessageDialog(AccountCancellationPanel.this, "Account canceled successfully");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(AccountCancellationPanel.this, "Failed to cancel account");
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
        add(cancelAccountButton, constraints);
    }

    private boolean cancelAccount(String email, String password) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/javaFinal";
        String username = "root";
        String dbPassword = "root";

        // 验证账户凭据
        if (!validateCredentials(email, password)) {
            return false;
        }

        // 删除账户的SQL语句
        String deleteQuery = "DELETE FROM users WHERE email=?";

        try (Connection connection = DriverManager.getConnection(url, username, dbPassword);
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, email);

            // 执行删除操作
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean validateCredentials(String email, String password) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/javaFinal";
        String username = "root";
        String dbPassword = "root";

        // 查询准备语句
        String query = "SELECT * FROM users WHERE email=? AND password=?";

        try (Connection connection = DriverManager.getConnection(url, username, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, password);

            // 执行查询
            try (ResultSet resultSet = statement.executeQuery()) {
                // 检查结果集是否有行
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