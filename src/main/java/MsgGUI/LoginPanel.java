package MsgGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginPanel() {
        setLayout(new GridLayout(1, 1));

        // 创建登录面板
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        // 创建组件
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        // 创建GridBagConstraints对象来设置组件的位置和大小
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);

        // 添加组件到登录面板
        panel.add(emailLabel, constraints);

        constraints.gridx = 1;
        panel.add(emailField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(passwordLabel, constraints);

        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, constraints);

        // 设置登录按钮的点击事件处理
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                // 验证邮箱和密码
                boolean loginSuccessful = validateCredentials(email, password);

                if (loginSuccessful) {
                    // 登录成功
                    JOptionPane.showMessageDialog(LoginPanel.this, "Login successful!");
                    // 执行登录后的操作
                } else {
                    // 登录失败
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid email or password. Please try again.");
                }

                // 清空密码框
                passwordField.setText("");
            }
        });

        add(panel);
    }

    private boolean validateCredentials(String email, String password) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/javaFinal";
        String username = "root";
        String dbPassword = "root";

        // 查询准备语句
        String query = "SELECT * FROM users WHERE email=? AND password=?";

        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(url, username, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, password);

            // 执行查询
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // 账号存在且凭据验证成功
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

// 账号不存在或凭据验证失败
        return false;
    }
}