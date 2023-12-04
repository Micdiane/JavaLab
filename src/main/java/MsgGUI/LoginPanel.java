package MsgGUI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPanel extends GridPane {
    private TextField emailField;
    private PasswordField passwordField;

    public User currentUser;

    public LoginPanel() {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        Label emailLabel = new Label("Email:");
        Label passwordLabel = new Label("Password:");
        emailField = new TextField();
        passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        add(emailLabel, 0, 0);
        add(emailField, 1, 0);

        add(passwordLabel, 0, 1);
        add(passwordField, 1, 1);

        add(loginButton, 0, 2, 2, 1);

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            boolean loginSuccessful = validateCredentials(email, password);

            if (loginSuccessful) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Login successful!");
                alert.showAndWait();
                // 执行登录后的操作
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid email or password. Please try again.");
                alert.showAndWait();
            }

            passwordField.setText("");
        });
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
                currentUser = new User(resultSet.getString("email"), resultSet.getString("password"));
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

    public Object getCurrentUser() {
        return currentUser;
    }
}