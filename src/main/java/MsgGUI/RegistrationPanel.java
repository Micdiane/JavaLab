package MsgGUI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationPanel extends GridPane {
    private TextField emailTextField;
    private PasswordField passwordField;

    public RegistrationPanel() {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        Label emailLabel = new Label("Email:");
        emailTextField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            String email = emailTextField.getText();
            String password = passwordField.getText();

            if (registerUser(email, password)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("User registered successfully");
                alert.showAndWait();
                clearFields();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Failed to register user");
                alert.showAndWait();
            }
        });

        add(emailLabel, 0, 0);
        add(emailTextField, 1, 0);

        add(passwordLabel, 0, 1);
        add(passwordField, 1, 1);

        add(registerButton, 0, 2, 2, 1);
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